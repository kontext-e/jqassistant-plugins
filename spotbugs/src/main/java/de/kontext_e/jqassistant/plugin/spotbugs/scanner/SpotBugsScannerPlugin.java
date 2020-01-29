package de.kontext_e.jqassistant.plugin.spotbugs.scanner;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.spotbugs.jaxb.*;
import de.kontext_e.jqassistant.plugin.spotbugs.store.descriptor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author jn4, Kontext E GmbH, 05.02.14
 */
@ScannerPlugin.Requires(FileDescriptor.class)
public class SpotBugsScannerPlugin extends AbstractScannerPlugin<FileResource, SpotBugsReportDescriptor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpotBugsScannerPlugin.class);
    public static final String JQASSISTANT_PLUGIN_SPOTBUGS_FILENAME = "jqassistant.plugin.spotbugs.filename";
    public static final String JQASSISTANT_PLUGIN_SPOTBUGS_DIRNAME = "jqassistant.plugin.spotbugs.dirname";

    private JAXBContext jaxbContext;

    private static String findBugsFileName = "findbugs.xml";
    private static String findBugsDirName = "findbugs";
    private static String spotBugsFileName = "spotbugs.xml";
    private static String spotBugsDirName = "spotbugs";

    public SpotBugsScannerPlugin() {
        try {
            jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot create JAXB context.", e);
        }
    }

    @Override
    protected void configure() {
        super.configure();

        if(getProperties().containsKey(JQASSISTANT_PLUGIN_SPOTBUGS_FILENAME)) {
            findBugsFileName = (String) getProperties().get(JQASSISTANT_PLUGIN_SPOTBUGS_FILENAME);
        }
        if(getProperties().containsKey(JQASSISTANT_PLUGIN_SPOTBUGS_DIRNAME)) {
            findBugsDirName = (String) getProperties().get(JQASSISTANT_PLUGIN_SPOTBUGS_DIRNAME);
        }
        LOGGER.info(String.format("SpotBugs plugin looks for files named %s or for all XML files in directories named %s", spotBugsFileName, spotBugsDirName));
        LOGGER.info(String.format("SpotBugs plugin also still looks for files named %s or for all XML files in directories named %s", findBugsFileName, findBugsDirName));
    }

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) {
        try {
            boolean accepted = acceptsPath(path);
            if (accepted) {
                LOGGER.info(String.format("SpotBugs plugin accepted file %s", path));
            }
            return accepted;
        } catch (Exception e) {
            LOGGER.error("Error while checking path: "+e, e);
            return false;
        }
    }

    boolean acceptsPath(String path) {
        return path.endsWith(findBugsFileName)
                || path.endsWith(spotBugsFileName)
                || parentDirectoryHasAcceptableName(path, findBugsDirName)
                || parentDirectoryHasAcceptableName(path, spotBugsDirName)
                ;
    }

    private boolean parentDirectoryHasAcceptableName(String path, String dirName) {
        if(!path.endsWith(".xml")) {
            return false;
        }

        final String[] parts = path.split("/");
        if(parts == null || parts.length < 2) {
            return false;
        }

        if(dirName == null) {
            return false;
        }

        String parentName = parts[parts.length - 2];

        return dirName.equalsIgnoreCase(parentName);
    }

    @Override
    public SpotBugsReportDescriptor scan(final FileResource file, String path, Scope scope, Scanner scanner) throws IOException {
        LOGGER.debug(String.format("SpotBugs scans file %s", path));
        final BugCollectionType bugCollectionType = unmarshalSpotBugsXml(file.createStream());
		FileDescriptor fileDescriptor = scanner.getContext().getCurrentDescriptor();
		final SpotBugsReportDescriptor spotBugsReportDescriptor = scanner.getContext().getStore().addDescriptorType(fileDescriptor, SpotBugsReportDescriptor.class);
        addBugInstancesToFindBugsDescriptor(scanner.getContext().getStore(), bugCollectionType, spotBugsReportDescriptor);
        return spotBugsReportDescriptor;
    }

    protected BugCollectionType unmarshalSpotBugsXml(final InputStream streamSource) throws IOException {
        BugCollectionType bugCollectionType;
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            bugCollectionType = unmarshaller.unmarshal(new StreamSource(streamSource), BugCollectionType.class).getValue();
        } catch (JAXBException e) {
            throw new IOException("Cannot read model descriptor.", e);
        }
        return bugCollectionType;
    }

    protected void addBugInstancesToFindBugsDescriptor(final Store store, final BugCollectionType bugCollectionType, final SpotBugsReportDescriptor spotBugsReportDescriptor) {
        for (BugInstanceType bugInstanceType : bugCollectionType.getBugInstance()) {
            SpotBugsBugInstanceDescriptor spotBugsBugInstanceDescriptor = store.create(SpotBugsBugInstanceDescriptor.class);

			fillBugInstanceDescriptor(store, bugInstanceType, spotBugsBugInstanceDescriptor, null);

			spotBugsReportDescriptor.getContains().add(spotBugsBugInstanceDescriptor);
        }

        for (FileType fileType : bugCollectionType.getFile()) {
			for (BugInstanceType bugInstanceType : fileType.getBugInstance()) {
				SpotBugsBugInstanceDescriptor spotBugsBugInstanceDescriptor = store.create(SpotBugsBugInstanceDescriptor.class);

				fillBugInstanceDescriptor(store, bugInstanceType, spotBugsBugInstanceDescriptor, fileType.getClassname());

				spotBugsReportDescriptor.getContains().add(spotBugsBugInstanceDescriptor);
			}
        }
    }

	private void fillBugInstanceDescriptor(final Store store, final BugInstanceType bugInstanceType, final SpotBugsBugInstanceDescriptor spotBugsBugInstanceDescriptor, final String classname) {
		spotBugsBugInstanceDescriptor.setType(bugInstanceType.getType());
		spotBugsBugInstanceDescriptor.setPriority(bugInstanceType.getPriority());
		spotBugsBugInstanceDescriptor.setAbbrev(bugInstanceType.getAbbrev());
		spotBugsBugInstanceDescriptor.setCategory(bugInstanceType.getCategory());
		spotBugsBugInstanceDescriptor.setMessage(bugInstanceType.getMessage());
		spotBugsBugInstanceDescriptor.setLineNumber(bugInstanceType.getLineNumber());
		spotBugsBugInstanceDescriptor.setClassName(classname);

		if(bugInstanceType.getClazz() != null) {
			final FindBugsBugInstanceClassDescriptor findBugsBugInstanceClassDescriptor = store.create(FindBugsBugInstanceClassDescriptor.class);
			final SourceLineType bugInstanceTypeSourceLine = bugInstanceType.getClazz().getSourceLine();
			final FindBugsSourceLineDescriptor sourceLineDescriptor = createSourceLineDescriptor(store, bugInstanceTypeSourceLine);
			findBugsBugInstanceClassDescriptor.setSourceLineDescriptor(sourceLineDescriptor);
			spotBugsBugInstanceDescriptor.setBugInstanceClass(findBugsBugInstanceClassDescriptor);
		}

		if(bugInstanceType.getMethod() != null) {
			for (MethodType methodType : bugInstanceType.getMethod()) {
				final FindBugsBugInstanceMethodDescriptor findBugsBugInstanceMethodDescriptor = store.create(FindBugsBugInstanceMethodDescriptor.class);
				findBugsBugInstanceMethodDescriptor.setFullQualifiedName(methodType.getClassname());
				findBugsBugInstanceMethodDescriptor.setName(methodType.getName());
				findBugsBugInstanceMethodDescriptor.setSignature(methodType.getSignature());
				findBugsBugInstanceMethodDescriptor.setIsStatic(Boolean.valueOf(methodType.getIsStatic()));
				findBugsBugInstanceMethodDescriptor.setSourceLineDescriptor(createSourceLineDescriptor(store, methodType.getSourceLine()));
				spotBugsBugInstanceDescriptor.getBugInstanceMethods().add(findBugsBugInstanceMethodDescriptor);
			}
		}

		if(bugInstanceType.getField() != null) {
			for (FieldType fieldType : bugInstanceType.getField()) {
				final FindBugsBugInstanceFieldDescriptor findBugsBugInstanceFieldDescriptor = store.create(FindBugsBugInstanceFieldDescriptor.class);
				findBugsBugInstanceFieldDescriptor.setFullQualifiedName(fieldType.getClassname());
				findBugsBugInstanceFieldDescriptor.setName(fieldType.getName());
				findBugsBugInstanceFieldDescriptor.setSignature(fieldType.getSignature());
				findBugsBugInstanceFieldDescriptor.setIsStatic(Boolean.valueOf(fieldType.getIsStatic()));
				findBugsBugInstanceFieldDescriptor.setSourceLineDescriptor(createSourceLineDescriptor(store, fieldType.getSourceLine()));
				spotBugsBugInstanceDescriptor.getBugInstanceFields().add(findBugsBugInstanceFieldDescriptor);
			}
		}

		if(bugInstanceType.getSourceLine() != null) {
			final SourceLineType bugInstanceTypeSourceLine = bugInstanceType.getSourceLine();
			final FindBugsSourceLineDescriptor sourceLineDescriptor = createSourceLineDescriptor(store, bugInstanceTypeSourceLine);
			spotBugsBugInstanceDescriptor.setSourceLineDescriptor(sourceLineDescriptor);
		}
	}

	private FindBugsSourceLineDescriptor createSourceLineDescriptor(final Store store, final SourceLineType bugInstanceTypeSourceLine) {
        final FindBugsSourceLineDescriptor sourceLineDescriptor = store.create(FindBugsSourceLineDescriptor.class);
        if(bugInstanceTypeSourceLine != null) {
            sourceLineDescriptor.setClassname(bugInstanceTypeSourceLine.getClassname());
            sourceLineDescriptor.setStart(bugInstanceTypeSourceLine.getStart());
            sourceLineDescriptor.setEnd(bugInstanceTypeSourceLine.getEnd());
            sourceLineDescriptor.setSourcefile(bugInstanceTypeSourceLine.getSourcefile());
            sourceLineDescriptor.setSourcepath(bugInstanceTypeSourceLine.getSourcepath());
        }
        return sourceLineDescriptor;
    }

    protected void writeFindBugsDescriptor(final String path, final BugCollectionType bugCollectionType, final SpotBugsReportDescriptor spotBugsReportDescriptor) {
        spotBugsReportDescriptor.setName(path);
        spotBugsReportDescriptor.setFileName(path);
        spotBugsReportDescriptor.setVersion(bugCollectionType.getVersion());
        spotBugsReportDescriptor.setSequence(bugCollectionType.getSequence());
        spotBugsReportDescriptor.setAnalysisTimestamp(bugCollectionType.getAnalysisTimestamp());
    }
}
