package de.kontext_e.jqassistant.plugin.checkstyle.scanner;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.scanner.FileSystemResource;
import com.buschmais.jqassistant.plugin.common.impl.scanner.AbstractScannerPlugin;
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
public class CheckstyleScannerPlugin extends AbstractScannerPlugin<FileSystemResource> {

    private JAXBContext jaxbContext;
    private static String basePackage = "com";

    public CheckstyleScannerPlugin() {
        try {
            jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot create JAXB context.", e);
        }
    }

    @Override
    public Class<? super FileSystemResource> getType() {
        return FileSystemResource.class;
    }

    @Override
    public boolean accepts(FileSystemResource item, String path, Scope scope) throws IOException {
        return !item.isDirectory() && path.endsWith("checkstyle.xml");
    }

    @Override
    public com.buschmais.jqassistant.core.store.api.type.FileDescriptor scan(FileSystemResource item, String path, Scope scope, Scanner scanner) throws IOException {
        final CheckstyleType checkstyleType = unmarshalCheckstyleXml(item.createStream());
        final CheckstyleDescriptor checkstyleDescriptor = getStore().create(CheckstyleDescriptor.class);
        checkstyleDescriptor.setFileName(path);
        readFiles(getStore(), checkstyleType, checkstyleDescriptor);
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

    should not build
    protected String convertToFullQualifiedName(final String name) {
        final String separator = "/".equals(System.getProperty("file.separator")) ? "/" :"\\\\";
        final String normalizedName = name.replaceAll(separator, ".");
        if(!normalizedName.contains(basePackage)) {
            System.err.println(String.format("Normalized name %s does not contain base package %s", normalizedName, basePackage));
            return "FQN.ERROR";
        }
        if(normalizedName.length() < 5) {
            System.err.println(String.format("Normalized name %s is shorter as expected", normalizedName));
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
    public void initialize() {

        final String property = (String) getProperties().get("jqassistant.plugin.checkstyle.basepackage");
        if(property != null) {
            basePackage = property;
        }
    }
}
