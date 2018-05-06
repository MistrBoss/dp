
package cz.sa.dovolena.server.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for sFileSource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sFileSource">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.server.dovolena.sa.cz/}soapUjo">
 *       &lt;sequence>
 *         &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="bytes" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="dateCleared" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="dateCreated" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="dateDeleted" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="dateModified" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="group" type="{http://ws.server.dovolena.sa.cz/}sFileSource" minOccurs="0"/>
 *         &lt;element name="hash" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="optimalSharpnessCoeficient" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="overSharpness" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="relativePath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sharpness" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="size" type="{http://ws.server.dovolena.sa.cz/}size" minOccurs="0"/>
 *         &lt;element name="src" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userCreated" type="{http://ws.server.dovolena.sa.cz/}sUser" minOccurs="0"/>
 *         &lt;element name="userDeleted" type="{http://ws.server.dovolena.sa.cz/}sUser" minOccurs="0"/>
 *         &lt;element name="userModified" type="{http://ws.server.dovolena.sa.cz/}sUser" minOccurs="0"/>
 *         &lt;element name="width" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sFileSource", propOrder = {
    "active",
    "bytes",
    "dateCleared",
    "dateCreated",
    "dateDeleted",
    "dateModified",
    "description",
    "group",
    "hash",
    "height",
    "id",
    "optimalSharpnessCoeficient",
    "overSharpness",
    "relativePath",
    "sharpness",
    "size",
    "src",
    "userCreated",
    "userDeleted",
    "userModified",
    "width"
})
public class SFileSource
    extends SoapUjo
{

    protected Boolean active;
    protected Long bytes;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateCleared;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateCreated;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateDeleted;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateModified;
    protected String description;
    protected SFileSource group;
    protected String hash;
    protected Integer height;
    protected Long id;
    protected Double optimalSharpnessCoeficient;
    protected Integer overSharpness;
    protected String relativePath;
    protected Integer sharpness;
    protected Size size;
    protected String src;
    protected SUser userCreated;
    protected SUser userDeleted;
    protected SUser userModified;
    protected Integer width;

    /**
     * Gets the value of the active property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setActive(Boolean value) {
        this.active = value;
    }

    /**
     * Gets the value of the bytes property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBytes() {
        return bytes;
    }

    /**
     * Sets the value of the bytes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBytes(Long value) {
        this.bytes = value;
    }

    /**
     * Gets the value of the dateCleared property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateCleared() {
        return dateCleared;
    }

    /**
     * Sets the value of the dateCleared property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateCleared(XMLGregorianCalendar value) {
        this.dateCleared = value;
    }

    /**
     * Gets the value of the dateCreated property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateCreated() {
        return dateCreated;
    }

    /**
     * Sets the value of the dateCreated property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateCreated(XMLGregorianCalendar value) {
        this.dateCreated = value;
    }

    /**
     * Gets the value of the dateDeleted property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDeleted() {
        return dateDeleted;
    }

    /**
     * Sets the value of the dateDeleted property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDeleted(XMLGregorianCalendar value) {
        this.dateDeleted = value;
    }

    /**
     * Gets the value of the dateModified property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateModified() {
        return dateModified;
    }

    /**
     * Sets the value of the dateModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateModified(XMLGregorianCalendar value) {
        this.dateModified = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the group property.
     * 
     * @return
     *     possible object is
     *     {@link SFileSource }
     *     
     */
    public SFileSource getGroup() {
        return group;
    }

    /**
     * Sets the value of the group property.
     * 
     * @param value
     *     allowed object is
     *     {@link SFileSource }
     *     
     */
    public void setGroup(SFileSource value) {
        this.group = value;
    }

    /**
     * Gets the value of the hash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the value of the hash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHash(String value) {
        this.hash = value;
    }

    /**
     * Gets the value of the height property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHeight(Integer value) {
        this.height = value;
    }

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
     * Gets the value of the optimalSharpnessCoeficient property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getOptimalSharpnessCoeficient() {
        return optimalSharpnessCoeficient;
    }

    /**
     * Sets the value of the optimalSharpnessCoeficient property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setOptimalSharpnessCoeficient(Double value) {
        this.optimalSharpnessCoeficient = value;
    }

    /**
     * Gets the value of the overSharpness property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverSharpness() {
        return overSharpness;
    }

    /**
     * Sets the value of the overSharpness property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverSharpness(Integer value) {
        this.overSharpness = value;
    }

    /**
     * Gets the value of the relativePath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelativePath() {
        return relativePath;
    }

    /**
     * Sets the value of the relativePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelativePath(String value) {
        this.relativePath = value;
    }

    /**
     * Gets the value of the sharpness property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSharpness() {
        return sharpness;
    }

    /**
     * Sets the value of the sharpness property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSharpness(Integer value) {
        this.sharpness = value;
    }

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link Size }
     *     
     */
    public Size getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link Size }
     *     
     */
    public void setSize(Size value) {
        this.size = value;
    }

    /**
     * Gets the value of the src property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrc() {
        return src;
    }

    /**
     * Sets the value of the src property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrc(String value) {
        this.src = value;
    }

    /**
     * Gets the value of the userCreated property.
     * 
     * @return
     *     possible object is
     *     {@link SUser }
     *     
     */
    public SUser getUserCreated() {
        return userCreated;
    }

    /**
     * Sets the value of the userCreated property.
     * 
     * @param value
     *     allowed object is
     *     {@link SUser }
     *     
     */
    public void setUserCreated(SUser value) {
        this.userCreated = value;
    }

    /**
     * Gets the value of the userDeleted property.
     * 
     * @return
     *     possible object is
     *     {@link SUser }
     *     
     */
    public SUser getUserDeleted() {
        return userDeleted;
    }

    /**
     * Sets the value of the userDeleted property.
     * 
     * @param value
     *     allowed object is
     *     {@link SUser }
     *     
     */
    public void setUserDeleted(SUser value) {
        this.userDeleted = value;
    }

    /**
     * Gets the value of the userModified property.
     * 
     * @return
     *     possible object is
     *     {@link SUser }
     *     
     */
    public SUser getUserModified() {
        return userModified;
    }

    /**
     * Sets the value of the userModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link SUser }
     *     
     */
    public void setUserModified(SUser value) {
        this.userModified = value;
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setWidth(Integer value) {
        this.width = value;
    }

}
