package de.kontext_e.jqassistant.plugin.jacoco.scanner;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import de.kontext_e.jqassistant.plugin.jacoco.jaxb.*;
import de.kontext_e.jqassistant.plugin.jacoco.store.descriptor.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
public class JacocoScannerPlugin extends AbstractScannerPlugin<FileResource,JacocoDescriptor > {

    private JAXBContext jaxbContext;

    public JacocoScannerPlugin() {
        try {
            jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot create JAXB context.", e);
        }
    }

    @Override
    public boolean accepts(final FileResource item, String path, Scope scope) throws IOException {
        String jacocoFileName = (String) getProperties().get("jqassistant.plugin.jacoco.filename");
        if(jacocoFileName == null) jacocoFileName = "jacoco.xml";
        return path.endsWith(jacocoFileName);
    }

    @Override
    public JacocoDescriptor scan(final FileResource file, String path, Scope scope, Scanner scanner) throws IOException {
        final JacocoDescriptor jacocoDescriptor = scanner.getContext().getStore().create(JacocoDescriptor.class);
        jacocoDescriptor.setFileName(path);
        final ReportType reportType = unmarshalJacocoXml(file.createStream());
        readPackages(scanner.getContext().getStore(), reportType, jacocoDescriptor);
        return jacocoDescriptor;
    }

    private void readPackages(final Store store, final ReportType reportType, final JacocoDescriptor jacocoDescriptor) {
        for (PackageType packageType : reportType.getPackage()) {
            final PackageDescriptor packageDescriptor = store.create(PackageDescriptor.class);
            packageDescriptor.setName(packageType.getName());
            readClasses(store, packageType, packageDescriptor);
            jacocoDescriptor.getJacocoPackages().add(packageDescriptor);
        }
    }

    private void readClasses(final Store store, final PackageType packageType, final PackageDescriptor packageDescriptor) {
        for (ClassType classType : packageType.getClazz()) {
            final ClassDescriptor classDescriptor = store.create(ClassDescriptor.class);
            classDescriptor.setName(classType.getName());
            classDescriptor.setFullQualifiedName(classDescriptor.getName().replaceAll("/", "."));
            readMethods(store, classType, classDescriptor);
            packageDescriptor.getJacocoClasses().add(classDescriptor);
        }
    }

    private void readMethods(final Store store, final ClassType classType, final ClassDescriptor classDescriptor) {
        for (MethodType methodType : classType.getMethod()) {
            final MethodDescriptor methodDescriptor = store.create(MethodDescriptor.class);
            methodDescriptor.setName(methodType.getName());
            methodDescriptor.setSignature(getMethodSignature(methodType.getName(), methodType.getDesc()));
            methodDescriptor.setLine(methodType.getLine());
            classDescriptor.getJacocoMethods().add(methodDescriptor);
            readCounters(store, methodType, methodDescriptor);
        }

    }

    private void readCounters(final Store store, final MethodType methodType, final MethodDescriptor methodDescriptor) {
        for (CounterType counterType : methodType.getCounter()) {
            final CounterDescriptor counterDescriptor = store.create(CounterDescriptor.class);
            counterDescriptor.setType(counterType.getType());
            counterDescriptor.setMissed(Long.valueOf(counterType.getMissed()));
            counterDescriptor.setCovered(Long.valueOf(counterType.getCovered()));
            methodDescriptor.getJacocoCounters().add(counterDescriptor);
        }
    }

    // copied from VisitorHelper, should be a common utility class
    String getMethodSignature(String name, String desc) {
        final StringBuilder signature = new StringBuilder();
        String returnType = org.objectweb.asm.Type.getReturnType(desc).getClassName();
        if (returnType != null) {
            signature.append(returnType);
            signature.append(' ');
        }
        signature.append(name);
        signature.append('(');
        org.objectweb.asm.Type[] types = org.objectweb.asm.Type.getArgumentTypes(desc);
        for (int i = 0; i < types.length; i++) {
            if (i > 0) {
                signature.append(',');
            }
            signature.append(types[i].getClassName());
        }
        signature.append(')');
        return signature.toString();
    }

    protected ReportType unmarshalJacocoXml(final InputStream streamSource) throws IOException {
        ReportType reportType;
        try {
            // use own SAXSource to prevent reading of jacoco's report.dtd
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setValidating(false);
            parserFactory.setFeature("http://xml.org/sax/features/validation", false);
            parserFactory.setFeature("http://apache.org/xml/features/validation/schema", false);
            parserFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            parserFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            XMLReader xmlReader = parserFactory.newSAXParser().getXMLReader();
            InputSource inputSource = new InputSource(new InputStreamReader(streamSource));
            SAXSource saxSource = new SAXSource(xmlReader, inputSource);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            reportType = unmarshaller.unmarshal(saxSource, ReportType.class).getValue();
        } catch (JAXBException |SAXException |ParserConfigurationException e ) {
            throw new IOException("Cannot read model descriptor.", e);
        }
        return reportType;
    }
}
