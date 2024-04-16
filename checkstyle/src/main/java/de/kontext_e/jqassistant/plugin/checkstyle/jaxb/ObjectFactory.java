//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.02.11 um 02:28:32 PM CET 
//


package de.kontext_e.jqassistant.plugin.checkstyle.jaxb;

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

    private static final QName _Checkstyle_QNAME = new QName("", "checkstyle");
    private static final QName _FileTypeError_QNAME = new QName("", "error");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CheckstyleType }
     * 
     */
    public CheckstyleType createCheckstyleType() {
        return new CheckstyleType();
    }

    /**
     * Create an instance of {@link FileType }
     * 
     */
    public FileType createFileType() {
        return new FileType();
    }

    /**
     * Create an instance of {@link ErrorType }
     * 
     */
    public ErrorType createErrorType() {
        return new ErrorType();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link CheckstyleType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "checkstyle")
    public JAXBElement<CheckstyleType> createCheckstyle(CheckstyleType value) {
        return new JAXBElement<>(_Checkstyle_QNAME, CheckstyleType.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ErrorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "error", scope = FileType.class)
    public JAXBElement<ErrorType> createFileTypeError(ErrorType value) {
        return new JAXBElement<>(_FileTypeError_QNAME, ErrorType.class, FileType.class, value);
    }

}
