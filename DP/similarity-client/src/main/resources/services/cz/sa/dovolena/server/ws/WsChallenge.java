
package cz.sa.dovolena.server.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for wsChallenge complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wsChallenge">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="user_salt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="challenge_salt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsChallenge", propOrder = {
    "id",
    "userSalt",
    "challengeSalt"
})
public class WsChallenge {

    protected Long id;
    @XmlElement(name = "user_salt")
    protected String userSalt;
    @XmlElement(name = "challenge_salt")
    protected String challengeSalt;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the userSalt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserSalt() {
        return userSalt;
    }

    /**
     * Sets the value of the userSalt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserSalt(String value) {
        this.userSalt = value;
    }

    /**
     * Gets the value of the challengeSalt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChallengeSalt() {
        return challengeSalt;
    }

    /**
     * Sets the value of the challengeSalt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChallengeSalt(String value) {
        this.challengeSalt = value;
    }

}
