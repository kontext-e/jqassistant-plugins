package de.kontext_e.jqassistant.plugin.checkstyle.scanner;

import java.io.IOException;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.buschmais.jqassistant.core.scanner.api.FileScannerPlugin;
import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.checkstyle.jaxb.CheckstyleType;
import de.kontext_e.jqassistant.plugin.checkstyle.jaxb.ErrorType;
import de.kontext_e.jqassistant.plugin.checkstyle.jaxb.FileType;
import de.kontext_e.jqassistant.plugin.checkstyle.jaxb.ObjectFactory;
import de.kontext_e.jqassistant.plugin.checkstyle.store.descriptor.CheckstyleDescriptor;
import de.kontext_e.jqassistant.plugin.checkstyle.store.descriptor.ErrorDescriptor;
import de.kontext_e.jqassistant.plugin.checkstyle.store.descriptor.FileDescriptor;


/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
public class CheckstyleScannerPlugin implements FileScannerPlugin {

    private JAXBContext jaxbContext;
    private Store store;
    private static String basePackage = "com";

    public CheckstyleScannerPlugin() {
        try {
            jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot create JAXB context.", e);
        }
    }

    @Override
    public boolean matches(final String file, final boolean isDirectory) {
        return !isDirectory && file.endsWith("checkstyle.xml");
    }

    @Override
    public CheckstyleDescriptor scanFile(final StreamSource streamSource) throws IOException {
        final CheckstyleType checkstyleType = unmarshalCheckstyleXml(streamSource);
        final CheckstyleDescriptor checkstyleDescriptor = store.create(CheckstyleDescriptor.class);
        checkstyleDescriptor.setFileName(streamSource.getSystemId());
        readFiles(store, checkstyleType, checkstyleDescriptor);
        return checkstyleDescriptor;
    }

    private void readFiles(final Store store, final CheckstyleType checkstyleType, final CheckstyleDescriptor checkstyleDescriptor) {
        for (FileType fileType : checkstyleType.getFile()) {
            final FileDescriptor fileDescriptor = store.create(FileDescriptor.class);
            fileDescriptor.setName(truncateName(fileType.getName()));
            fileDescriptor.setFullQualifiedName(convertToFullQualifiedName(fileType.getName()));
            checkstyleDescriptor.getFiles().add(fileDescriptor);
            readErrors(store, fileType, fileDescriptor);
        }
    }

    private void readErrors(final Store store, final FileType fileType, final FileDescriptor fileDescriptor) {
        for (ErrorType errorType : fileType.getError()) {
            final ErrorDescriptor errorDescriptor = store.create(ErrorDescriptor.class);
            errorDescriptor.setLine(errorType.getLine());
            errorDescriptor.setColumn(errorType.getColumn());
            errorDescriptor.setSeverity(errorType.getSeverity());
            errorDescriptor.setMessage(errorType.getMessage());
            errorDescriptor.setSource(errorType.getSource());
            fileDescriptor.getErrors().add(errorDescriptor);
        }
    }

    protected String convertToFullQualifiedName(final String name) {
        final String separator = "/".equals(System.getProperty("file.separator")) ? "/" :"\\\\";
        final String normalizedName = name.replaceAll(separator, ".");
        return normalizedName.substring(normalizedName.indexOf(basePackage), normalizedName.length() - 5);
    }

    protected String truncateName(final String name) {
        if(name.lastIndexOf(System.getProperty("file.separator")) <= 0) {
            return name;
        }
        return name.substring(name.lastIndexOf(System.getProperty("file.separator")) + 1);
    }

    protected CheckstyleType unmarshalCheckstyleXml(final StreamSource streamSource) throws IOException {
        final CheckstyleType checkstyleType;
        try {
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            checkstyleType = unmarshaller.unmarshal(streamSource, CheckstyleType.class).getValue();
        } catch (JAXBException e) {
            throw new IOException("Cannot read model descriptor.", e);
        }
        return checkstyleType;
    }

    @Override
    public CheckstyleDescriptor scanDirectory(final String name) throws IOException {
        return null;
    }

    @Override
    public void initialize(Store store, Properties properties) {
        this.store = store;

        final String property = properties.getProperty("jqassistant.plugin.checkstyle.basepackage");
        if(property != null) {
            basePackage = property;
        }
    }
}
