
package cz.sa.dovolena.server.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for submitResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="submitResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="slave_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="result" type="{http://ws.server.dovolena.sa.cz/}similarityData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submitResult", propOrder = {
    "slaveId",
    "result"
})
public class SubmitResult {

    @XmlElement(name = "slave_id")
    protected String slaveId;
    protected SimilarityData result;

    /**
     * Gets the value of the slaveId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSlaveId() {
        return slaveId;
    }

    /**
     * Sets the value of the slaveId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSlaveId(String value) {
        this.slaveId = value;
    }

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link SimilarityData }
     *     
     */
    public SimilarityData getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimilarityData }
     *     
     */
    public void setResult(SimilarityData value) {
        this.result = value;
    }

}
