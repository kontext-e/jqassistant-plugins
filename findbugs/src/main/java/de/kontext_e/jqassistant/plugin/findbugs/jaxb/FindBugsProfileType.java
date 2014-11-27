
package de.kontext_e.jqassistant.plugin.findbugs.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p&gt;Java class for FindBugsProfileType complex type.
 * 
 * <p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre&gt;
 * &lt;complexType name="FindBugsProfileType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ClassProfile" type="{}ClassProfileType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindBugsProfileType", propOrder = {
    "classProfile"
})
public class FindBugsProfileType {

    @XmlElement(name = "ClassProfile")
    protected List<ClassProfileType> classProfile;

    /**
     * Gets the value of the classProfile property.
     * 
     * <p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE&gt;set</CODE&gt; method for the classProfile property.
     * 
     * <p&gt;
     * For example, to add a new item, do as follows:
     * <pre&gt;
     *    getClassProfile().add(newItem);
     * </pre&gt;
     * 
     * 
     * <p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link ClassProfileType }
     * 
     * 
     */
    public List<ClassProfileType> getClassProfile() {
        if (classProfile == null) {
            classProfile = new ArrayList<ClassProfileType>();
        }
        return this.classProfile;
    }

}
