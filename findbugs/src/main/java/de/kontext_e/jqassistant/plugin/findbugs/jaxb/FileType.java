package de.kontext_e.jqassistant.plugin.findbugs.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileType")
public class FileType {
	@XmlElement(name = "BugInstance")
	protected List<BugInstanceType> bugInstance;
	@XmlAttribute(name = "classname")
	protected String classname;

	/**
	 * Gets the value of the bugInstance property.
	 *
	 * <p&gt;
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE&gt;set</CODE&gt; method for the bugInstance property.
	 *
	 * <p&gt;
	 * For example, to add a new item, do as follows:
	 * <pre&gt;
	 *    getBugInstance().add(newItem);
	 * </pre&gt;
	 *
	 *
	 * <p&gt;
	 * Objects of the following type(s) are allowed in the list
	 * {@link BugInstanceType }
	 *
	 *
	 */
	public List<BugInstanceType> getBugInstance() {
		if (bugInstance == null) {
			bugInstance = new ArrayList<BugInstanceType>();
		}
		return this.bugInstance;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(final String classname) {
		this.classname = classname;
	}
}
