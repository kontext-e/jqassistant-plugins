
package de.kontext_e.jqassistant.plugin.findbugs.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p&gt;Java class for ProjectType complex type.
 * 
 * <p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre&gt;
 * &lt;complexType name="ProjectType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Jar" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="AuxClasspathEntry" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="SrcDir" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="C:\Users\jn\projects\timng\infrastructure\src\test\java\de\kontext_e\tim\infrastructure\queries\EmbeddedEntity.java"/&gt;
 *               &lt;enumeration value="C:\Users\jn\projects\timng\infrastructure\src\test\java\de\kontext_e\tim\infrastructure\queries\JpaTableNameTest.java"/&gt;
 *               &lt;enumeration value="C:\Users\jn\projects\timng\infrastructure\src\test\java\de\kontext_e\tim\infrastructure\queries\QueriesTest.java"/&gt;
 *               &lt;enumeration value="C:\Users\jn\projects\timng\infrastructure\src\test\java\de\kontext_e\tim\infrastructure\queries\QueryBuilderTest.java"/&gt;
 *               &lt;enumeration value="C:\Users\jn\projects\timng\infrastructure\src\test\java\de\kontext_e\tim\infrastructure\queries\TestEntity.java"/&gt;
 *               &lt;enumeration value="C:\Users\jn\projects\timng\infrastructure\src\test\java\de\kontext_e\tim\infrastructure\tools\BuildVersionTest.java"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="projectName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProjectType", propOrder = {
    "jar",
    "auxClasspathEntry",
    "srcDir"
})
public class ProjectType {

    @XmlElement(name = "Jar")
    protected List<String> jar;
    @XmlElement(name = "AuxClasspathEntry")
    protected List<String> auxClasspathEntry;
    @XmlElement(name = "SrcDir")
    protected List<String> srcDir;
    @XmlAttribute(name = "projectName")
    protected String projectName;

    /**
     * Gets the value of the jar property.
     * 
     * <p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE&gt;set</CODE&gt; method for the jar property.
     * 
     * <p&gt;
     * For example, to add a new item, do as follows:
     * <pre&gt;
     *    getJar().add(newItem);
     * </pre&gt;
     * 
     * 
     * <p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getJar() {
        if (jar == null) {
            jar = new ArrayList<String>();
        }
        return this.jar;
    }

    /**
     * Gets the value of the auxClasspathEntry property.
     * 
     * <p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE&gt;set</CODE&gt; method for the auxClasspathEntry property.
     * 
     * <p&gt;
     * For example, to add a new item, do as follows:
     * <pre&gt;
     *    getAuxClasspathEntry().add(newItem);
     * </pre&gt;
     * 
     * 
     * <p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAuxClasspathEntry() {
        if (auxClasspathEntry == null) {
            auxClasspathEntry = new ArrayList<String>();
        }
        return this.auxClasspathEntry;
    }

    /**
     * Gets the value of the srcDir property.
     * 
     * <p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE&gt;set</CODE&gt; method for the srcDir property.
     * 
     * <p&gt;
     * For example, to add a new item, do as follows:
     * <pre&gt;
     *    getSrcDir().add(newItem);
     * </pre&gt;
     * 
     * 
     * <p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSrcDir() {
        if (srcDir == null) {
            srcDir = new ArrayList<String>();
        }
        return this.srcDir;
    }

    /**
     * Gets the value of the projectName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets the value of the projectName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProjectName(String value) {
        this.projectName = value;
    }

}
