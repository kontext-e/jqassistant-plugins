package de.kontext_e.jqassistant.plugin.findbugs.scanner;

import java.io.IOException;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.buschmais.jqassistant.core.scanner.api.FileScannerPlugin;
import com.buschmais.jqassistant.core.store.api.Store;
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
public class FindBugsScannerPlugin implements FileScannerPlugin {

    private static String findBugsFileName = "findbugs.xml";

    private JAXBContext jaxbContext;
    private Store store;

    public FindBugsScannerPlugin() {
        try {
            jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot create JAXB context.", e);
        }
    }

    @Override
    public boolean matches(final String file, final boolean isDirectory) {
        // hm, matching based on file name seems not so clever, could have any name!?
        return !isDirectory && file.endsWith(findBugsFileName);
    }

    @Override
    public FindBugsDescriptor scanFile(final StreamSource streamSource) throws IOException {
        final BugCollectionType bugCollectionType = unmarshalFindBugsXml(streamSource);
        final FindBugsDescriptor findBugsDescriptor = store.create(FindBugsDescriptor.class);
        writeFindBugsDescriptor(streamSource, bugCollectionType, findBugsDescriptor);
        addBugInstancesToFindBugsDescriptor(store, bugCollectionType, findBugsDescriptor);
        return findBugsDescriptor;
    }

    protected BugCollectionType unmarshalFindBugsXml(final StreamSource streamSource) throws IOException {
        BugCollectionType bugCollectionType;
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            bugCollectionType = unmarshaller.unmarshal(streamSource, BugCollectionType.class).getValue();
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

    protected void writeFindBugsDescriptor(final StreamSource streamSource, final BugCollectionType bugCollectionType, final FindBugsDescriptor findBugsDescriptor) {
        findBugsDescriptor.setName(streamSource.getSystemId());
        findBugsDescriptor.setFileName(streamSource.getSystemId());
        findBugsDescriptor.setVersion(bugCollectionType.getVersion());
        findBugsDescriptor.setSequence(bugCollectionType.getSequence());
        findBugsDescriptor.setAnalysisTimestamp(bugCollectionType.getAnalysisTimestamp());
    }

    @Override
    public FindBugsDescriptor scanDirectory(final String name) throws IOException {
        // that's correct, we don't want to scan directories
        return null;
    }

    @Override
    public void initialize(Store store, Properties properties) {
        this.store = store;

        final String property = properties.getProperty("jqassistant.plugin.findbugs.filename");
        if(property != null) {
            findBugsFileName = property;
        }
    }
}
