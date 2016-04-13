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
import de.kontext_e.jqassistant.plugin.pmd.store.PmdFileDescriptor;
import de.kontext_e.jqassistant.plugin.pmd.store.PmdReportDescriptor;
import de.kontext_e.jqassistant.plugin.pmd.store.PmdViolationDescriptor;


/**
 * @author aw, Kontext E GmbH, 29.01.15
 */
public class PmdReportScannerPlugin extends AbstractScannerPlugin<FileResource, PmdReportDescriptor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmdReportScannerPlugin.class);
     public static final String JQASSISTANT_PLUGIN_PMD_FILENAME = "jqassistant.plugin.pmd.filename";
     public static final String JQASSISTANT_PLUGIN_PMD_DIRNAME = "jqassistant.plugin.pmd.dirname";

    private JAXBContext jaxbContext;
    private String pmdFileName = "pmd.xml";
    private String pmdDirName = "pmd";

    public PmdReportScannerPlugin() {
        try {
            jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot create JAXB context.", e);
        }
    }

    @Override
    protected void configure() {
        super.configure();

        if(getProperties().containsKey(JQASSISTANT_PLUGIN_PMD_FILENAME)) {
            pmdFileName = (String) getProperties().get(JQASSISTANT_PLUGIN_PMD_FILENAME);
        }
        if(System.getProperty(JQASSISTANT_PLUGIN_PMD_FILENAME) != null) {
            pmdFileName = System.getProperty(JQASSISTANT_PLUGIN_PMD_FILENAME);
        }
        if(getProperties().containsKey(JQASSISTANT_PLUGIN_PMD_DIRNAME)) {
            pmdDirName = (String) getProperties().get(JQASSISTANT_PLUGIN_PMD_DIRNAME);
        }
        if(System.getProperty(JQASSISTANT_PLUGIN_PMD_DIRNAME) != null) {
            pmdDirName = System.getProperty(JQASSISTANT_PLUGIN_PMD_DIRNAME);
        }
        LOGGER.info(String.format("PMD plugin looks for files named %s or for all XML files in directories named '%s'", pmdFileName, pmdDirName));
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
    public PmdReportDescriptor scan(final FileResource file, String path, Scope scope, Scanner scanner) throws IOException {
        LOGGER.debug("Pmd scans path "+path);
        final PmdType pmdType = unmarshalPmdXml(file.createStream());
        final PmdReportDescriptor pmdReportDescriptor = scanner.getContext().getStore().create(PmdReportDescriptor.class);
        pmdReportDescriptor.setFileName(path);
        readFiles(scanner.getContext().getStore(), pmdType, pmdReportDescriptor);
        return pmdReportDescriptor;
    }

    private void readFiles(final Store store, final PmdType pmdType, final PmdReportDescriptor pmdReportDescriptor) {
        for (FileType fileType : pmdType.getFile()) {
            final PmdFileDescriptor pmdFileDescriptor = store.create(PmdFileDescriptor.class);
            pmdFileDescriptor.setName(truncateName(fileType.getName()));
            
            //create fqn from first Violation element attributes:
            String classname = fileType.getViolation().get(0).getClazz();
            String packagename = fileType.getViolation().get(0).getPackage();
            String fqn = packagename+"."+classname;            
            pmdFileDescriptor.setFullQualifiedName(fqn);
            
            pmdReportDescriptor.getFiles().add(pmdFileDescriptor);
            readViolations(store, fileType, pmdFileDescriptor);
        }
    }

    private void readViolations(final Store store, final FileType fileType, final PmdFileDescriptor pmdFileDescriptor) {
        for (ViolationType vioType : fileType.getViolation()) {
            final PmdViolationDescriptor vioDescriptor = store.create(PmdViolationDescriptor.class);
            vioDescriptor.setRule(vioType.getRule());
            vioDescriptor.setRuleSet(vioType.getRuleset());
            vioDescriptor.setPriority(vioType.getPriority());
            vioDescriptor.setMethod(vioType.getMethod());
            pmdFileDescriptor.getViolations().add(vioDescriptor);
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
