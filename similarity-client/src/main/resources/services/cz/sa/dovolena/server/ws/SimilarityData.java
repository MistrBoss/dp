
package cz.sa.dovolena.server.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for similarityData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="similarityData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pattern" type="{http://ws.server.dovolena.sa.cz/}sFileSource" minOccurs="0"/>
 *         &lt;element name="compare" type="{http://ws.server.dovolena.sa.cz/}sFileSource" minOccurs="0"/>
 *         &lt;element name="similarity" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "similarityData", propOrder = {
    "pattern",
    "compare",
    "similarity"
})
public class SimilarityData {

    protected SFileSource pattern;
    protected SFileSource compare;
    protected Double similarity;

    /**
     * Gets the value of the pattern property.
     * 
     * @return
     *     possible object is
     *     {@link SFileSource }
     *     
     */
    public SFileSource getPattern() {
        return pattern;
    }

    /**
     * Sets the value of the pattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link SFileSource }
     *     
     */
    public void setPattern(SFileSource value) {
        this.pattern = value;
    }

    /**
     * Gets the value of the compare property.
     * 
     * @return
     *     possible object is
     *     {@link SFileSource }
     *     
     */
    public SFileSource getCompare() {
        return compare;
    }

    /**
     * Sets the value of the compare property.
     * 
     * @param value
     *     allowed object is
     *     {@link SFileSource }
     *     
     */
    public void setCompare(SFileSource value) {
        this.compare = value;
    }

    /**
     * Gets the value of the similarity property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSimilarity() {
        return similarity;
    }

    /**
     * Sets the value of the similarity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSimilarity(Double value) {
        this.similarity = value;
    }

}
