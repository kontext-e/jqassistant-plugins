package de.kontext_e.jqassistant.plugin.pmd.scanner;

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
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;

import de.kontext_e.jqassistant.plugin.pmd.jaxb.FileType;
import de.kontext_e.jqassistant.plugin.pmd.jaxb.ObjectFactory;
import de.kontext_e.jqassistant.plugin.pmd.jaxb.PmdType;
import de.kontext_e.jqassistant.plugin.pmd.jaxb.ViolationType;
import de.kontext_e.jqassistant.plugin.pmd.store.FileDescriptor;
import de.kontext_e.jqassistant.plugin.pmd.store.PmdDescriptor;
import de.kontext_e.jqassistant.plugin.pmd.store.ViolationDescriptor;


/**
 * @author aw, Kontext E GmbH, 29.01.15
 */
public class PmdScannerPlugin extends AbstractScannerPlugin<FileResource, PmdDescriptor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmdScannerPlugin.class);
     public static final String JQASSISTANT_PLUGIN_PMD_FILENAME = "jqassistant.plugin.pmd.filename";

    private JAXBContext jaxbContext;
    private static String basePackage = "org";
    private String pmdFileName = "pmd.xml";
    private String pmdDirName = "pmd";

    public PmdScannerPlugin() {
        try {
            jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot create JAXB context.", e);
        }
    }
    
    @Override
    protected void initialize() {
        final String property = (String) getProperties().get(JQASSISTANT_PLUGIN_PMD_FILENAME);
        if(property != null) {
            pmdFileName = property;
        }
        if(System.getProperty(JQASSISTANT_PLUGIN_PMD_FILENAME) != null) {
            pmdFileName = System.getProperty(JQASSISTANT_PLUGIN_PMD_FILENAME);
        }
        LOGGER.info(String.format("PMD plugin looks for files named %s or for all XML files in directories named 'findbugs'", pmdFileName));
    }

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) throws IOException {
        boolean accepted = path.endsWith(pmdFileName) || (pmdDirName.equals(item.getFile().toPath().getParent().toFile().getName()) && path.endsWith(".xml"));
        if(accepted) {
            LOGGER.debug("Pmd accepted path "+path);
        }
        return accepted;
    }

    @Override
    public PmdDescriptor scan(final FileResource file, String path, Scope scope, Scanner scanner) throws IOException {
        LOGGER.debug("Pmd scans path "+path);
        final PmdType pmdType = unmarshalPmdXml(file.createStream());
        final PmdDescriptor pmdDescriptor = scanner.getContext().getStore().create(PmdDescriptor.class);
        pmdDescriptor.setFileName(path);
        readFiles(scanner.getContext().getStore(), pmdType, pmdDescriptor);
        return pmdDescriptor;
    }

    private void readFiles(final Store store, final PmdType pmdType, final PmdDescriptor pmdDescriptor) {
        for (FileType fileType : pmdType.getFile()) {
            final FileDescriptor fileDescriptor = store.create(FileDescriptor.class);
            fileDescriptor.setName(truncateName(fileType.getName()));
            //fileDescriptor.setFullQualifiedName(convertToFullQualifiedName(fileType.getName()));
            pmdDescriptor.getFiles().add(fileDescriptor);
            readViolations(store, fileType, fileDescriptor);
        }
    }

    private void readViolations(final Store store, final FileType fileType, final FileDescriptor fileDescriptor) {
        for (ViolationType vioType : fileType.getViolation()) {
            final ViolationDescriptor vioDescriptor = store.create(ViolationDescriptor.class);
            vioDescriptor.setRule(vioType.getRule());
            vioDescriptor.setRuleSet(vioType.getRuleset());
            vioDescriptor.setPriority(vioType.getPriority());
            vioDescriptor.setMethod(vioType.getMethod());
            fileDescriptor.getViolations().add(vioDescriptor);
        }
    }

    protected String truncateName(final String name) {
        if(name.lastIndexOf(System.getProperty("file.separator")) <= 0) {
            return name;
        }
        return name.substring(name.lastIndexOf(System.getProperty("file.separator")) + 1);
    }

    protected PmdType unmarshalPmdXml(final InputStream streamSource) throws IOException {
        final PmdType pmdType;
        try {
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            pmdType = unmarshaller.unmarshal(new StreamSource(streamSource), PmdType.class).getValue();
        } catch (JAXBException e) {
            throw new IOException("Cannot read model descriptor.", e);
        }
        return pmdType;
    }

   
}
