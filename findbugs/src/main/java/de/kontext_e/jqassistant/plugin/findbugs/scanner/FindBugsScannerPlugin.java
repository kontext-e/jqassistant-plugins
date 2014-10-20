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
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.core.store.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.VirtualFile;
import com.buschmais.jqassistant.plugin.common.impl.scanner.AbstractScannerPlugin;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.BugCollectionType;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.BugInstanceType;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.ObjectFactory;
import de.kontext_e.jqassistant.plugin.findbugs.jaxb.SourceLineType;
import de.kontext_e.jqassistant.plugin.findbugs.store.descriptor.BugInstanceDescriptor;
import de.kontext_e.jqassistant.plugin.findbugs.store.descriptor.FindBugsDescriptor;
import de.kontext_e.jqassistant.plugin.findbugs.store.descriptor.SourceLineDescriptor;

/**
 * @author jn4, Kontext E GmbH, 05.02.14
 */
public class FindBugsScannerPlugin extends AbstractScannerPlugin<VirtualFile> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FindBugsScannerPlugin.class);

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
    protected void initialize() {
        super.initialize();
        final String property = (String) getProperties().get("jqassistant.plugin.findbugs.filename");
        if(property != null) {
            findBugsFileName = property;
        }
    }

    @Override
    public boolean accepts(VirtualFile item, String path, Scope scope) throws IOException {
        return path.endsWith(findBugsFileName);
    }

    @Override
    public FileDescriptor scan(final VirtualFile file, String path, Scope scope, Scanner scanner) throws IOException {
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

            final SourceLineType bugInstanceTypeSourceLine = bugInstanceType.getSourceLine();
            final SourceLineDescriptor sourceLineDescriptor = store.create(SourceLineDescriptor.class);
            sourceLineDescriptor.setClassname(bugInstanceTypeSourceLine.getClassname());
            sourceLineDescriptor.setStart(bugInstanceTypeSourceLine.getStart());
            sourceLineDescriptor.setEnd(bugInstanceTypeSourceLine.getEnd());
            sourceLineDescriptor.setSourcefile(bugInstanceTypeSourceLine.getSourcefile());
            sourceLineDescriptor.setSourcepath(bugInstanceTypeSourceLine.getSourcepath());
            bugInstanceDescriptor.setSourceLineDescriptor(sourceLineDescriptor);

            findBugsDescriptor.getContains().add(bugInstanceDescriptor);
        }
    }

    protected void writeFindBugsDescriptor(final String path, final BugCollectionType bugCollectionType, final FindBugsDescriptor findBugsDescriptor) {
        findBugsDescriptor.setName(path);
        findBugsDescriptor.setFileName(path);
        findBugsDescriptor.setVersion(bugCollectionType.getVersion());
        findBugsDescriptor.setSequence(bugCollectionType.getSequence());
        findBugsDescriptor.setAnalysisTimestamp(bugCollectionType.getAnalysisTimestamp());
    }
}
