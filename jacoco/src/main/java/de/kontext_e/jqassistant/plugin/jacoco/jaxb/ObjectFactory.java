//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.02.11 um 01:30:27 PM CET 
//


package de.kontext_e.jqassistant.plugin.jacoco.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Report_QNAME = new QName("", "report");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReportType }
     *
     */
    public ReportType createReportType() {
        return new ReportType();
    }

    /**
     * Create an instance of {@link LineType }
     *
     */
    public LineType createLineType() {
        return new LineType();
    }

    /**
     * Create an instance of {@link de.kontext_e.jqassistant.plugin.jacoco.jaxb.SessioninfoType }
     *
     */
    public SessioninfoType createSessioninfoType() {
        return new SessioninfoType();
    }

    /**
     * Create an instance of {@link PackageType }
     *
     */
    public PackageType createPackageType() {
        return new PackageType();
    }

    /**
     * Create an instance of {@link de.kontext_e.jqassistant.plugin.jacoco.jaxb.MethodType }
     *
     */
    public MethodType createMethodType() {
        return new MethodType();
    }

    /**
     * Create an instance of {@link SourcefileType }
     *
     */
    public SourcefileType createSourcefileType() {
        return new SourcefileType();
    }

    /**
     * Create an instance of {@link de.kontext_e.jqassistant.plugin.jacoco.jaxb.CounterType }
     *
     */
    public CounterType createCounterType() {
        return new CounterType();
    }

    /**
     * Create an instance of {@link de.kontext_e.jqassistant.plugin.jacoco.jaxb.ClassType }
     *
     */
    public ClassType createClassType() {
        return new ClassType();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ReportType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "report")
    public JAXBElement<ReportType> createReport(ReportType value) {
        return new JAXBElement<>(_Report_QNAME, ReportType.class, null, value);
    }

}
