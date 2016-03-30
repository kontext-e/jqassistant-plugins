package de.kontext_e.jqassistant.plugin.jacoco.scanner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.jacoco.jaxb.ClassType;
import de.kontext_e.jqassistant.plugin.jacoco.jaxb.CounterType;
import de.kontext_e.jqassistant.plugin.jacoco.jaxb.MethodType;
import de.kontext_e.jqassistant.plugin.jacoco.jaxb.ObjectFactory;
import de.kontext_e.jqassistant.plugin.jacoco.jaxb.PackageType;
import de.kontext_e.jqassistant.plugin.jacoco.jaxb.ReportType;
import de.kontext_e.jqassistant.plugin.jacoco.store.descriptor.JacocoClassDescriptor;
import de.kontext_e.jqassistant.plugin.jacoco.store.descriptor.JacocoCounterDescriptor;
import de.kontext_e.jqassistant.plugin.jacoco.store.descriptor.JacocoReportDescriptor;
import de.kontext_e.jqassistant.plugin.jacoco.store.descriptor.JacocoMethodDescriptor;
import de.kontext_e.jqassistant.plugin.jacoco.store.descriptor.JacocoPackageDescriptor;

/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
public class JacocoScannerPlugin extends AbstractScannerPlugin<FileResource,JacocoReportDescriptor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JacocoScannerPlugin.class);
    public static final String JQASSISTANT_PLUGIN_JACOCO_FILENAME = "jqassistant.plugin.jacoco.filename";
    private JAXBContext jaxbContext;
    private String jacocoFileName = "jacocoTestReport.xml";

    public JacocoScannerPlugin() {
        try {
            jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot create JAXB context.", e);
        }
    }

    @Override
    protected void configure() {
        super.configure();

        String jacocoFileNameProperty = (String) getProperties().get(JQASSISTANT_PLUGIN_JACOCO_FILENAME);
        if(jacocoFileNameProperty != null) {
            jacocoFileName = jacocoFileNameProperty;
        }
        if(System.getProperty(JQASSISTANT_PLUGIN_JACOCO_FILENAME) != null) {
            jacocoFileName = System.getProperty(JQASSISTANT_PLUGIN_JACOCO_FILENAME);
        }
        LOGGER.info("Jacoco plugin looks for files named "+jacocoFileName+" and files in directory jacoco");
    }

    @Override
    public boolean accepts(final FileResource item, String path, Scope scope) throws IOException {
        boolean accepted = path.endsWith(jacocoFileName) || ("jacoco".equalsIgnoreCase(item.getFile().toPath().getParent().toFile().getName()) && path.endsWith(".xml"));
        if(accepted) {
            LOGGER.debug("Jacoco plugin accepted "+path);
        }
        return accepted;
    }

    @Override
    public JacocoReportDescriptor scan(final FileResource file, String path, Scope scope, Scanner scanner) throws IOException {
        LOGGER.debug("Jacoco plugin scans "+path);
        final JacocoReportDescriptor jacocoReportDescriptor = scanner.getContext().getStore().create(JacocoReportDescriptor.class);
        jacocoReportDescriptor.setFileName(path);
        final ReportType reportType = unmarshalJacocoXml(file.createStream());
        readPackages(scanner.getContext().getStore(), reportType, jacocoReportDescriptor);
        return jacocoReportDescriptor;
    }

    private void readPackages(final Store store, final ReportType reportType, final JacocoReportDescriptor jacocoReportDescriptor) {
        for (PackageType packageType : reportType.getPackage()) {
            final JacocoPackageDescriptor jacocoPackageDescriptor = store.create(JacocoPackageDescriptor.class);
            jacocoPackageDescriptor.setName(packageType.getName());
            readClasses(store, packageType, jacocoPackageDescriptor);
            jacocoReportDescriptor.getJacocoPackages().add(jacocoPackageDescriptor);
        }
    }

    private void readClasses(final Store store, final PackageType packageType, final JacocoPackageDescriptor jacocoPackageDescriptor) {
        for (ClassType classType : packageType.getClazz()) {
            final JacocoClassDescriptor jacocoClassDescriptor = store.create(JacocoClassDescriptor.class);
            jacocoClassDescriptor.setName(classType.getName());
            jacocoClassDescriptor.setFullQualifiedName(jacocoClassDescriptor.getName().replaceAll("/", "."));
            readMethods(store, classType, jacocoClassDescriptor);
            jacocoPackageDescriptor.getJacocoClasses().add(jacocoClassDescriptor);
        }
    }

    private void readMethods(final Store store, final ClassType classType, final JacocoClassDescriptor jacocoClassDescriptor) {
        for (MethodType methodType : classType.getMethod()) {
            final JacocoMethodDescriptor jacocoMethodDescriptor = store.create(JacocoMethodDescriptor.class);
            jacocoMethodDescriptor.setName(methodType.getName());
            jacocoMethodDescriptor.setSignature(getMethodSignature(methodType.getName(), methodType.getDesc()));
            jacocoMethodDescriptor.setLine(methodType.getLine());
            jacocoClassDescriptor.getJacocoMethods().add(jacocoMethodDescriptor);
            readCounters(store, methodType, jacocoMethodDescriptor);
        }

    }

    private void readCounters(final Store store, final MethodType methodType, final JacocoMethodDescriptor jacocoMethodDescriptor) {
        for (CounterType counterType : methodType.getCounter()) {
            final JacocoCounterDescriptor jacocoCounterDescriptor = store.create(JacocoCounterDescriptor.class);
            jacocoCounterDescriptor.setType(counterType.getType());
            jacocoCounterDescriptor.setMissed(Long.valueOf(counterType.getMissed()));
            jacocoCounterDescriptor.setCovered(Long.valueOf(counterType.getCovered()));
            jacocoMethodDescriptor.getJacocoCounters().add(jacocoCounterDescriptor);
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
