//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.01.29 um 12:04:41 PM CET 
//


package de.kontext_e.jqassistant.plugin.pmd.jaxb;

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

    private final static QName _Pmd_QNAME = new QName("", "pmd");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PmdType }
     * 
     */
    public PmdType createPmdType() {
        return new PmdType();
    }

    /**
     * Create an instance of {@link ViolationType }
     * 
     */
    public ViolationType createViolationType() {
        return new ViolationType();
    }

    /**
     * Create an instance of {@link FileType }
     * 
     */
    public FileType createFileType() {
        return new FileType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PmdType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pmd")
    public JAXBElement<PmdType> createPmd(PmdType value) {
        return new JAXBElement<PmdType>(_Pmd_QNAME, PmdType.class, null, value);
    }

}
