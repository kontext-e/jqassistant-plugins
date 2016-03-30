package de.kontext_e.jqassistant.plugin.checkstyle.scanner;

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
import de.kontext_e.jqassistant.plugin.checkstyle.jaxb.CheckstyleType;
import de.kontext_e.jqassistant.plugin.checkstyle.jaxb.ErrorType;
import de.kontext_e.jqassistant.plugin.checkstyle.jaxb.FileType;
import de.kontext_e.jqassistant.plugin.checkstyle.jaxb.ObjectFactory;
import de.kontext_e.jqassistant.plugin.checkstyle.store.descriptor.CheckstyleReportDescriptor;
import de.kontext_e.jqassistant.plugin.checkstyle.store.descriptor.CheckstyleErrorDescriptor;
import de.kontext_e.jqassistant.plugin.checkstyle.store.descriptor.CheckstyleFileDescriptor;


/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
public class CheckstyleScannerPlugin extends AbstractScannerPlugin<FileResource, CheckstyleReportDescriptor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckstyleScannerPlugin.class);
    public static final String JQASSISTANT_PLUGIN_CHECKSTYLE_BASEPACKAGE = "jqassistant.plugin.checkstyle.basepackage";
    public static final String JQASSISTANT_PLUGIN_CHECKSTYLE_FILENAME = "jqassistant.plugin.checkstyle.filename";
    public static final String JQASSISTANT_PLUGIN_CHECKSTYLE_DIRNAME = "jqassistant.plugin.checkstyle.dirname";
    private JAXBContext jaxbContext;
    private static String basePackage = "org";
    private String checkstyleFileName = "checkstyle.xml";
    private String checkstyleDirName = "checkstyle";

    public CheckstyleScannerPlugin() {
        try {
            jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot create JAXB context.", e);
        }
    }

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) throws IOException {
        boolean accepted = path.endsWith(checkstyleFileName) || (checkstyleDirName.equals(item.getFile().toPath().getParent().toFile().getName()) && path.endsWith(".xml"));
        if(accepted) {
            LOGGER.debug("Checkstyle accepted path "+path);
        }
        return accepted;
    }

    @Override
    public CheckstyleReportDescriptor scan(final FileResource file, String path, Scope scope, Scanner scanner) throws IOException {
        LOGGER.debug("Checkstyle scans path "+path);
        final CheckstyleType checkstyleType = unmarshalCheckstyleXml(file.createStream());
        final CheckstyleReportDescriptor checkstyleReportDescriptor = scanner.getContext().getStore().create(CheckstyleReportDescriptor.class);
        checkstyleReportDescriptor.setFileName(path);
        readFiles(scanner.getContext().getStore(), checkstyleType, checkstyleReportDescriptor);
        return checkstyleReportDescriptor;
    }

    private void readFiles(final Store store, final CheckstyleType checkstyleType, final CheckstyleReportDescriptor checkstyleReportDescriptor) {
        for (FileType fileType : checkstyleType.getFile()) {
            final CheckstyleFileDescriptor checkstyleFileDescriptor = store.create(CheckstyleFileDescriptor.class);
            checkstyleFileDescriptor.setName(truncateName(fileType.getName()));
            checkstyleFileDescriptor.setFullQualifiedName(convertToFullQualifiedName(fileType.getName()));
            checkstyleReportDescriptor.getFiles().add(checkstyleFileDescriptor);
            readErrors(store, fileType, checkstyleFileDescriptor);
        }
    }

    private void readErrors(final Store store, final FileType fileType, final CheckstyleFileDescriptor checkstyleFileDescriptor) {
        for (ErrorType errorType : fileType.getError()) {
            final CheckstyleErrorDescriptor checkstyleErrorDescriptor = store.create(CheckstyleErrorDescriptor.class);
            checkstyleErrorDescriptor.setLine(errorType.getLine());
            checkstyleErrorDescriptor.setColumn(errorType.getColumn());
            checkstyleErrorDescriptor.setSeverity(errorType.getSeverity());
            checkstyleErrorDescriptor.setMessage(errorType.getMessage());
            checkstyleErrorDescriptor.setSource(errorType.getSource());
            checkstyleFileDescriptor.getErrors().add(checkstyleErrorDescriptor);
        }
    }

    protected String convertToFullQualifiedName(final String name) {
        final String separator = "/".equals(System.getProperty("file.separator")) ? "/" :"\\\\";
        final String normalizedName = name.replaceAll(separator, ".");
        if(!normalizedName.contains(basePackage)) {
            LOGGER.error(String.format("Normalized name %s does not contain base package %s", normalizedName, basePackage));
            return "FQN.ERROR";
        }
        if(normalizedName.length() < 5) {
            LOGGER.error(String.format("Normalized name %s is shorter as expected", normalizedName));
            return "FQN.ERROR";
        }
        return normalizedName.substring(normalizedName.indexOf(basePackage), normalizedName.length() - 5);
    }

    protected String truncateName(final String name) {
        if(name.lastIndexOf(System.getProperty("file.separator")) <= 0) {
            return name;
        }
        return name.substring(name.lastIndexOf(System.getProperty("file.separator")) + 1);
    }

    protected CheckstyleType unmarshalCheckstyleXml(final InputStream streamSource) throws IOException {
        final CheckstyleType checkstyleType;
        try {
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            checkstyleType = unmarshaller.unmarshal(new StreamSource(streamSource), CheckstyleType.class).getValue();
        } catch (JAXBException e) {
            throw new IOException("Cannot read model descriptor.", e);
        }
        return checkstyleType;
    }

    @Override
    protected void configure() {
        super.configure();

        final String property = (String) getProperties().get(JQASSISTANT_PLUGIN_CHECKSTYLE_BASEPACKAGE);
        if(property != null) {
            basePackage = property;
        }
        if(System.getProperty(JQASSISTANT_PLUGIN_CHECKSTYLE_BASEPACKAGE) != null) {
            basePackage = System.getProperty(JQASSISTANT_PLUGIN_CHECKSTYLE_BASEPACKAGE);
        }
        LOGGER.info("Checkstyle uses base package "+basePackage+" to determine the full qualified name. If the fqn property contains FQN.ERROR you should set property "+JQASSISTANT_PLUGIN_CHECKSTYLE_BASEPACKAGE+" to the base package of your application.");

        final String checkstyleFileNameProperty = (String) getProperties().get(JQASSISTANT_PLUGIN_CHECKSTYLE_FILENAME);
        if(checkstyleFileNameProperty != null) {
            checkstyleFileName = checkstyleFileNameProperty;
        }
        if(System.getProperty(JQASSISTANT_PLUGIN_CHECKSTYLE_FILENAME) != null) {
            checkstyleFileName = System.getProperty(JQASSISTANT_PLUGIN_CHECKSTYLE_FILENAME);
        }

        final String checkstyleDirNameProperty = (String) getProperties().get(JQASSISTANT_PLUGIN_CHECKSTYLE_DIRNAME);
        if(checkstyleDirNameProperty != null) {
            checkstyleDirName = checkstyleDirNameProperty;
        }
        if(System.getProperty(JQASSISTANT_PLUGIN_CHECKSTYLE_DIRNAME) != null) {
            checkstyleDirName = System.getProperty(JQASSISTANT_PLUGIN_CHECKSTYLE_DIRNAME);
        }
        LOGGER.info(String.format("Checkstyle plugin looks for files named %s or for all XML files in directories named '%s'", checkstyleFileName, checkstyleDirName));

    }
}
