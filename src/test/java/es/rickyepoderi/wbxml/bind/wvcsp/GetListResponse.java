//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.25 at 05:48:09 PM CEST 
//


package es.rickyepoderi.wbxml.bind.wvcsp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "contactList",
    "defaultContactList"
})
@XmlRootElement(name = "GetList-Response")
public class GetListResponse {

    @XmlElement(name = "ContactList")
    protected List<ContactList> contactList;
    @XmlElement(name = "DefaultContactList")
    protected String defaultContactList;

    /**
     * Gets the value of the contactList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contactList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContactList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContactList }
     * 
     * 
     */
    public List<ContactList> getContactList() {
        if (contactList == null) {
            contactList = new ArrayList<ContactList>();
        }
        return this.contactList;
    }

    /**
     * Gets the value of the defaultContactList property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultContactList() {
        return defaultContactList;
    }

    /**
     * Sets the value of the defaultContactList property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultContactList(String value) {
        this.defaultContactList = value;
    }

}
