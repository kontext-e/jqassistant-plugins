package de.kontext_e.jqassistant.plugin.findbugs.scanner;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.BugCollectionType;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.BugInstanceType;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.FieldType;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.FileType;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.MethodType;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.ObjectFactory;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.SourceLineType;
import de.kontext_e.jqassistant.plugin.findbugs.store.descriptor.FindBugsBugInstanceClassDescriptor;
import de.kontext_e.jqassistant.plugin.findbugs.store.descriptor.FindBugsBugInstanceDescriptor;
import de.kontext_e.jqassistant.plugin.findbugs.store.descriptor.FindBugsBugInstanceFieldDescriptor;
import de.kontext_e.jqassistant.plugin.findbugs.store.descriptor.FindBugsBugInstanceMethodDescriptor;
import de.kontext_e.jqassistant.plugin.findbugs.store.descriptor.FindBugsReportDescriptor;
import de.kontext_e.jqassistant.plugin.findbugs.store.descriptor.FindBugsSourceLineDescriptor;

/**
 * @author jn4, Kontext E GmbH, 05.02.14
 */
@ScannerPlugin.Requires(FileDescriptor.class)
public class FindBugsScannerPlugin extends AbstractScannerPlugin<FileResource, FindBugsReportDescriptor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FindBugsScannerPlugin.class);
    public static final String JQASSISTANT_PLUGIN_FINDBUGS_FILENAME = "jqassistant.plugin.findbugs.filename";
    public static final String JQASSISTANT_PLUGIN_FINDBUGS_DIRNAME = "jqassistant.plugin.findbugs.dirname";

    private JAXBContext jaxbContext;

    private static String findBugsFileName = "findbugs.xml";
    private static String findBugsDirName = "findbugs";

    public FindBugsScannerPlugin() {
        try {
            jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot create JAXB context.", e);
        }
    }

    @Override
    protected void configure() {
        super.configure();

        if(getProperties().containsKey(JQASSISTANT_PLUGIN_FINDBUGS_FILENAME)) {
            findBugsFileName = (String) getProperties().get(JQASSISTANT_PLUGIN_FINDBUGS_FILENAME);
        }
        if(getProperties().containsKey(JQASSISTANT_PLUGIN_FINDBUGS_DIRNAME)) {
            findBugsDirName = (String) getProperties().get(JQASSISTANT_PLUGIN_FINDBUGS_DIRNAME);
        }
        LOGGER.info(String.format("FindBugs plugin looks for files named %s or for all XML files in directories named %s", findBugsFileName, findBugsDirName));
    }

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) throws IOException {
        boolean accepted = path.endsWith(findBugsFileName) || (findBugsDirName.equals(item.getFile().toPath().getParent().toFile().getName()) && path.endsWith(".xml"));
        if(accepted) {
            LOGGER.debug(String.format("FindBugs accepted file %s", path));
        }
        return accepted;
    }

    @Override
    public FindBugsReportDescriptor scan(final FileResource file, String path, Scope scope, Scanner scanner) throws IOException {
        LOGGER.debug(String.format("FindBugs scans file %s", path));
        final BugCollectionType bugCollectionType = unmarshalFindBugsXml(file.createStream());
		FileDescriptor fileDescriptor = scanner.getContext().getCurrentDescriptor();
		final FindBugsReportDescriptor findBugsReportDescriptor = scanner.getContext().getStore().addDescriptorType(fileDescriptor, FindBugsReportDescriptor.class);
        addBugInstancesToFindBugsDescriptor(scanner.getContext().getStore(), bugCollectionType, findBugsReportDescriptor);
        return findBugsReportDescriptor;
    }

    protected BugCollectionType unmarshalFindBugsXml(final InputStream streamSource) throws IOException {
        BugCollectionType bugCollectionType;
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            bugCollectionType = unmarshaller.unmarshal(new StreamSource(streamSource), BugCollectionType.class).getValue();
        } catch (JAXBException e) {
            throw new IOException("Cannot read model descriptor.", e);
        }
        return bugCollectionType;
    }

    protected void addBugInstancesToFindBugsDescriptor(final Store store, final BugCollectionType bugCollectionType, final FindBugsReportDescriptor findBugsReportDescriptor) {
        for (BugInstanceType bugInstanceType : bugCollectionType.getBugInstance()) {
            FindBugsBugInstanceDescriptor findBugsBugInstanceDescriptor = store.create(FindBugsBugInstanceDescriptor.class);

			fillBugInstanceDescriptor(store, bugInstanceType, findBugsBugInstanceDescriptor, null);

			findBugsReportDescriptor.getContains().add(findBugsBugInstanceDescriptor);
        }

        for (FileType fileType : bugCollectionType.getFile()) {
			for (BugInstanceType bugInstanceType : fileType.getBugInstance()) {
				FindBugsBugInstanceDescriptor findBugsBugInstanceDescriptor = store.create(FindBugsBugInstanceDescriptor.class);

				fillBugInstanceDescriptor(store, bugInstanceType, findBugsBugInstanceDescriptor, fileType.getClassname());

				findBugsReportDescriptor.getContains().add(findBugsBugInstanceDescriptor);
			}
        }
    }

	private void fillBugInstanceDescriptor(final Store store, final BugInstanceType bugInstanceType, final FindBugsBugInstanceDescriptor findBugsBugInstanceDescriptor, final String classname) {
		findBugsBugInstanceDescriptor.setType(bugInstanceType.getType());
		findBugsBugInstanceDescriptor.setPriority(bugInstanceType.getPriority());
		findBugsBugInstanceDescriptor.setAbbrev(bugInstanceType.getAbbrev());
		findBugsBugInstanceDescriptor.setCategory(bugInstanceType.getCategory());
		findBugsBugInstanceDescriptor.setMessage(bugInstanceType.getMessage());
		findBugsBugInstanceDescriptor.setLineNumber(bugInstanceType.getLineNumber());
		findBugsBugInstanceDescriptor.setClassName(classname);

		if(bugInstanceType.getClazz() != null) {
			final FindBugsBugInstanceClassDescriptor findBugsBugInstanceClassDescriptor = store.create(FindBugsBugInstanceClassDescriptor.class);
			final SourceLineType bugInstanceTypeSourceLine = bugInstanceType.getClazz().getSourceLine();
			final FindBugsSourceLineDescriptor sourceLineDescriptor = createSourceLineDescriptor(store, bugInstanceTypeSourceLine);
			findBugsBugInstanceClassDescriptor.setSourceLineDescriptor(sourceLineDescriptor);
			findBugsBugInstanceDescriptor.setBugInstanceClass(findBugsBugInstanceClassDescriptor);
		}

		if(bugInstanceType.getMethod() != null) {
			for (MethodType methodType : bugInstanceType.getMethod()) {
				final FindBugsBugInstanceMethodDescriptor findBugsBugInstanceMethodDescriptor = store.create(FindBugsBugInstanceMethodDescriptor.class);
				findBugsBugInstanceMethodDescriptor.setFullQualifiedName(methodType.getClassname());
				findBugsBugInstanceMethodDescriptor.setName(methodType.getName());
				findBugsBugInstanceMethodDescriptor.setSignature(methodType.getSignature());
				findBugsBugInstanceMethodDescriptor.setIsStatic(Boolean.valueOf(methodType.getIsStatic()));
				findBugsBugInstanceMethodDescriptor.setSourceLineDescriptor(createSourceLineDescriptor(store, methodType.getSourceLine()));
				findBugsBugInstanceDescriptor.getBugInstanceMethods().add(findBugsBugInstanceMethodDescriptor);
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
				findBugsBugInstanceDescriptor.getBugInstanceFields().add(findBugsBugInstanceFieldDescriptor);
			}
		}

		if(bugInstanceType.getSourceLine() != null) {
			final SourceLineType bugInstanceTypeSourceLine = bugInstanceType.getSourceLine();
			final FindBugsSourceLineDescriptor sourceLineDescriptor = createSourceLineDescriptor(store, bugInstanceTypeSourceLine);
			findBugsBugInstanceDescriptor.setSourceLineDescriptor(sourceLineDescriptor);
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

    protected void writeFindBugsDescriptor(final String path, final BugCollectionType bugCollectionType, final FindBugsReportDescriptor findBugsReportDescriptor) {
        findBugsReportDescriptor.setName(path);
        findBugsReportDescriptor.setFileName(path);
        findBugsReportDescriptor.setVersion(bugCollectionType.getVersion());
        findBugsReportDescriptor.setSequence(bugCollectionType.getSequence());
        findBugsReportDescriptor.setAnalysisTimestamp(bugCollectionType.getAnalysisTimestamp());
    }
}
