package de.kontext_e.jqassistant.plugin.checkstyle.scanner;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import de.kontext_e.jqassistant.plugin.checkstyle.jaxb.CheckstyleType;
import de.kontext_e.jqassistant.plugin.checkstyle.jaxb.ErrorType;
import de.kontext_e.jqassistant.plugin.checkstyle.jaxb.FileType;
import de.kontext_e.jqassistant.plugin.checkstyle.jaxb.ObjectFactory;
import de.kontext_e.jqassistant.plugin.checkstyle.store.descriptor.CheckstyleDescriptor;
import de.kontext_e.jqassistant.plugin.checkstyle.store.descriptor.ErrorDescriptor;
import de.kontext_e.jqassistant.plugin.checkstyle.store.descriptor.FileDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
public class CheckstyleScannerPlugin extends AbstractScannerPlugin<FileResource, CheckstyleDescriptor> {

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
    public boolean accepts(FileResource item, String path, Scope scope) throws IOException {
        return path.endsWith("checkstyle.xml");
    }

    @Override
    public CheckstyleDescriptor scan(final FileResource file, String path, Scope scope, Scanner scanner) throws IOException {
        final CheckstyleType checkstyleType = unmarshalCheckstyleXml(file.createStream());
        final CheckstyleDescriptor checkstyleDescriptor = scanner.getContext().getStore().create(CheckstyleDescriptor.class);
        checkstyleDescriptor.setFileName(path);
        readFiles(scanner.getContext().getStore(), checkstyleType, checkstyleDescriptor);
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
    protected void initialize() {
        final String property = (String) getProperties().get("jqassistant.plugin.checkstyle.basepackage");
        if(property != null) {
            basePackage = property;
        }
    }
}
