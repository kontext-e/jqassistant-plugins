package de.kontext_e.jqassistant.plugin.findbugs.scanner;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.BugCollectionType;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.BugInstanceType;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.FieldType;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.MethodType;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.ObjectFactory;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.SourceLineType;
import de.kontext_e.jqassistant.plugin.findbugs.store.descriptor.BugInstanceClassDescriptor;
import de.kontext_e.jqassistant.plugin.findbugs.store.descriptor.BugInstanceDescriptor;
import de.kontext_e.jqassistant.plugin.findbugs.store.descriptor.BugInstanceFieldDescriptor;
import de.kontext_e.jqassistant.plugin.findbugs.store.descriptor.BugInstanceMethodDescriptor;
import de.kontext_e.jqassistant.plugin.findbugs.store.descriptor.FindBugsDescriptor;
import de.kontext_e.jqassistant.plugin.findbugs.store.descriptor.SourceLineDescriptor;
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
public class FindBugsScannerPlugin extends AbstractScannerPlugin<FileResource, FindBugsDescriptor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FindBugsScannerPlugin.class);
    public static final String JQASSISTANT_PLUGIN_FINDBUGS_FILENAME = "jqassistant.plugin.findbugs.filename";

    private JAXBContext jaxbContext;

    private static String findBugsFileName = "findbugs.xml";

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

        final String property = (String) getProperties().get(JQASSISTANT_PLUGIN_FINDBUGS_FILENAME);
        if(property != null) {
            findBugsFileName = property;
        }
        if(System.getProperty(JQASSISTANT_PLUGIN_FINDBUGS_FILENAME) != null) {
            findBugsFileName = System.getProperty(JQASSISTANT_PLUGIN_FINDBUGS_FILENAME);
        }
        LOGGER.info(String.format("FindBugs plugin looks for files named %s or for all XML files in directories named 'findbugs'", findBugsFileName));
    }

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) throws IOException {
        boolean accepted = path.endsWith(findBugsFileName) || ("findbugs".equals(item.getFile().toPath().getParent().toFile().getName()) && path.endsWith(".xml"));
        if(accepted) {
            LOGGER.debug(String.format("FindBugs accepted file %s", path));
        }
        return accepted;
    }

    @Override
    public FindBugsDescriptor scan(final FileResource file, String path, Scope scope, Scanner scanner) throws IOException {
        LOGGER.debug(String.format("FindBugs scans file %s", path));
        final BugCollectionType bugCollectionType = unmarshalFindBugsXml(file.createStream());
        final FindBugsDescriptor findBugsDescriptor = scanner.getContext().getStore().create(FindBugsDescriptor.class);
        writeFindBugsDescriptor(path, bugCollectionType, findBugsDescriptor);
        addBugInstancesToFindBugsDescriptor(scanner.getContext().getStore(), bugCollectionType, findBugsDescriptor);
        return findBugsDescriptor;
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

    protected void addBugInstancesToFindBugsDescriptor(final Store store, final BugCollectionType bugCollectionType, final FindBugsDescriptor findBugsDescriptor) {
        for (BugInstanceType bugInstanceType : bugCollectionType.getBugInstance()) {
            BugInstanceDescriptor bugInstanceDescriptor = store.create(BugInstanceDescriptor.class);

            bugInstanceDescriptor.setType(bugInstanceType.getType());
            bugInstanceDescriptor.setPriority(bugInstanceType.getPriority());
            bugInstanceDescriptor.setAbbrev(bugInstanceType.getAbbrev());
            bugInstanceDescriptor.setCategory(bugInstanceType.getCategory());

            if(bugInstanceType.getClazz() != null) {
                final BugInstanceClassDescriptor bugInstanceClassDescriptor = store.create(BugInstanceClassDescriptor.class);
                final SourceLineType bugInstanceTypeSourceLine = bugInstanceType.getClazz().getSourceLine();
                final SourceLineDescriptor sourceLineDescriptor = createSourceLineDescriptor(store, bugInstanceTypeSourceLine);
                bugInstanceClassDescriptor.setSourceLineDescriptor(sourceLineDescriptor);
                bugInstanceDescriptor.setBugInstanceClass(bugInstanceClassDescriptor);
            }

            if(bugInstanceType.getMethod() != null) {
                for (MethodType methodType : bugInstanceType.getMethod()) {
                    final BugInstanceMethodDescriptor bugInstanceMethodDescriptor = store.create(BugInstanceMethodDescriptor.class);
                    bugInstanceMethodDescriptor.setFullQualifiedName(methodType.getClassname());
                    bugInstanceMethodDescriptor.setName(methodType.getName());
                    bugInstanceMethodDescriptor.setSignature(methodType.getSignature());
                    bugInstanceMethodDescriptor.setIsStatic(Boolean.valueOf(methodType.getIsStatic()));
                    bugInstanceMethodDescriptor.setSourceLineDescriptor(createSourceLineDescriptor(store, methodType.getSourceLine()));
                    bugInstanceDescriptor.getBugInstanceMethods().add(bugInstanceMethodDescriptor);
                }
            }

            if(bugInstanceType.getField() != null) {
                for (FieldType fieldType : bugInstanceType.getField()) {
                    final BugInstanceFieldDescriptor bugInstanceFieldDescriptor = store.create(BugInstanceFieldDescriptor.class);
                    bugInstanceFieldDescriptor.setFullQualifiedName(fieldType.getClassname());
                    bugInstanceFieldDescriptor.setName(fieldType.getName());
                    bugInstanceFieldDescriptor.setSignature(fieldType.getSignature());
                    bugInstanceFieldDescriptor.setIsStatic(Boolean.valueOf(fieldType.getIsStatic()));
                    bugInstanceFieldDescriptor.setSourceLineDescriptor(createSourceLineDescriptor(store, fieldType.getSourceLine()));
                    bugInstanceDescriptor.getBugInstanceFields().add(bugInstanceFieldDescriptor);
                }
            }

            if(bugInstanceType.getSourceLine() != null) {
                final SourceLineType bugInstanceTypeSourceLine = bugInstanceType.getSourceLine();
                final SourceLineDescriptor sourceLineDescriptor = createSourceLineDescriptor(store, bugInstanceTypeSourceLine);
                bugInstanceDescriptor.setSourceLineDescriptor(sourceLineDescriptor);
            }

            findBugsDescriptor.getContains().add(bugInstanceDescriptor);
        }
    }

    private SourceLineDescriptor createSourceLineDescriptor(final Store store, final SourceLineType bugInstanceTypeSourceLine) {
        final SourceLineDescriptor sourceLineDescriptor = store.create(SourceLineDescriptor.class);
        if(bugInstanceTypeSourceLine != null) {
            sourceLineDescriptor.setClassname(bugInstanceTypeSourceLine.getClassname());
            sourceLineDescriptor.setStart(bugInstanceTypeSourceLine.getStart());
            sourceLineDescriptor.setEnd(bugInstanceTypeSourceLine.getEnd());
            sourceLineDescriptor.setSourcefile(bugInstanceTypeSourceLine.getSourcefile());
            sourceLineDescriptor.setSourcepath(bugInstanceTypeSourceLine.getSourcepath());
        }
        return sourceLineDescriptor;
    }

    protected void writeFindBugsDescriptor(final String path, final BugCollectionType bugCollectionType, final FindBugsDescriptor findBugsDescriptor) {
        findBugsDescriptor.setName(path);
        findBugsDescriptor.setFileName(path);
        findBugsDescriptor.setVersion(bugCollectionType.getVersion());
        findBugsDescriptor.setSequence(bugCollectionType.getSequence());
        findBugsDescriptor.setAnalysisTimestamp(bugCollectionType.getAnalysisTimestamp());
    }
}
