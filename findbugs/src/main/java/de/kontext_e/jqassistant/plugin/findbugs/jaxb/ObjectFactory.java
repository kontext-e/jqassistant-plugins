
package de.kontext_e.jqassistant.plugin.findbugs.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.kontext_e.jqassistant.plugin.findbugs.jaxb package. 
 * <p&gt;An ObjectFactory allows you to programatically
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

    private final static QName _BugCollection_QNAME = new QName("", "BugCollection");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.kontext_e.jqassistant.plugin.findbugs.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BugCollectionType }
     * 
     */
    public BugCollectionType createBugCollectionType() {
        return new BugCollectionType();
    }

    /**
     * Create an instance of {@link FieldType }
     * 
     */
    public FieldType createFieldType() {
        return new FieldType();
    }

    /**
     * Create an instance of {@link ClassProfileType }
     * 
     */
    public ClassProfileType createClassProfileType() {
        return new ClassProfileType();
    }

    /**
     * Create an instance of {@link ClassType }
     * 
     */
    public ClassType createClassType() {
        return new ClassType();
    }

    /**
     * Create an instance of {@link ErrorsType }
     * 
     */
    public ErrorsType createErrorsType() {
        return new ErrorsType();
    }

    /**
     * Create an instance of {@link ProjectType }
     * 
     */
    public ProjectType createProjectType() {
        return new ProjectType();
    }

    /**
     * Create an instance of {@link FindBugsSummaryType }
     * 
     */
    public FindBugsSummaryType createFindBugsSummaryType() {
        return new FindBugsSummaryType();
    }

    /**
     * Create an instance of {@link SourceLineType }
     * 
     */
    public SourceLineType createSourceLineType() {
        return new SourceLineType();
    }

    /**
     * Create an instance of {@link ClassStatsType }
     * 
     */
    public ClassStatsType createClassStatsType() {
        return new ClassStatsType();
    }

    /**
     * Create an instance of {@link MethodType }
     * 
     */
    public MethodType createMethodType() {
        return new MethodType();
    }

    /**
     * Create an instance of {@link FindBugsProfileType }
     * 
     */
    public FindBugsProfileType createFindBugsProfileType() {
        return new FindBugsProfileType();
    }

    /**
     * Create an instance of {@link PackageStatsType }
     * 
     */
    public PackageStatsType createPackageStatsType() {
        return new PackageStatsType();
    }

    /**
     * Create an instance of {@link BugInstanceType }
     * 
     */
    public BugInstanceType createBugInstanceType() {
        return new BugInstanceType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BugCollectionType }{@code &gt;}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "BugCollection")
    public JAXBElement<BugCollectionType> createBugCollection(BugCollectionType value) {
        return new JAXBElement<BugCollectionType>(_BugCollection_QNAME, BugCollectionType.class, null, value);
    }

}
