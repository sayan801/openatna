//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.05 at 10:53:27 AM BST 
//


package org.openhealthtools.openatna.anom.jaxb21.schema;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EventIdentification" type="{}EventIdentificationType"/>
 *         &lt;element name="ActiveParticipant" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{}ActiveParticipantType">
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="AuditSourceIdentification" type="{}AuditSourceIdentificationType" maxOccurs="unbounded"/>
 *         &lt;element name="ParticipantObjectIdentification" type="{}ParticipantObjectIdentificationType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "eventIdentification",
        "activeParticipant",
        "auditSourceIdentification",
        "participantObjectIdentification"
})
@XmlRootElement(name = "AuditMessage")
public class AuditMessage {

    @XmlElement(name = "EventIdentification", required = true)
    protected EventIdentificationType eventIdentification;
    @XmlElement(name = "ActiveParticipant", required = true)
    protected List<ActiveParticipantType> activeParticipant;
    @XmlElement(name = "AuditSourceIdentification", required = true)
    protected List<AuditSourceIdentificationType> auditSourceIdentification;
    @XmlElement(name = "ParticipantObjectIdentification")
    protected List<ParticipantObjectIdentificationType> participantObjectIdentification;

    /**
     * Gets the value of the eventIdentification property.
     *
     * @return possible object is
     *         {@link EventIdentificationType }
     */
    public EventIdentificationType getEventIdentification() {
        return eventIdentification;
    }

    /**
     * Sets the value of the eventIdentification property.
     *
     * @param value allowed object is
     *              {@link EventIdentificationType }
     */
    public void setEventIdentification(EventIdentificationType value) {
        this.eventIdentification = value;
    }

    /**
     * Gets the value of the activeParticipant property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the activeParticipant property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActiveParticipant().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link ActiveParticipantType }
     */
    public List<ActiveParticipantType> getActiveParticipant() {
        if (activeParticipant == null) {
            activeParticipant = new ArrayList<ActiveParticipantType>();
        }
        return this.activeParticipant;
    }

    /**
     * Gets the value of the auditSourceIdentification property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the auditSourceIdentification property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuditSourceIdentification().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link AuditSourceIdentificationType }
     */
    public List<AuditSourceIdentificationType> getAuditSourceIdentification() {
        if (auditSourceIdentification == null) {
            auditSourceIdentification = new ArrayList<AuditSourceIdentificationType>();
        }
        return this.auditSourceIdentification;
    }

    /**
     * Gets the value of the participantObjectIdentification property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the participantObjectIdentification property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParticipantObjectIdentification().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link ParticipantObjectIdentificationType }
     */
    public List<ParticipantObjectIdentificationType> getParticipantObjectIdentification() {
        if (participantObjectIdentification == null) {
            participantObjectIdentification = new ArrayList<ParticipantObjectIdentificationType>();
        }
        return this.participantObjectIdentification;
    }


}