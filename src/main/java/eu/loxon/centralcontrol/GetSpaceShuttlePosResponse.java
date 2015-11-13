package eu.loxon.centralcontrol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cord" type="{http://www.loxon.eu/CentralControl/}wsCoordinate"/>
 *         &lt;element name="result" type="{http://www.loxon.eu/CentralControl/}commonResp"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "cord", "result" })
@XmlRootElement(name = "getSpaceShuttlePosResponse")
public class GetSpaceShuttlePosResponse {

	@XmlElement(required = true)
	protected WsCoordinate cord;
	@XmlElement(required = true)
	protected CommonResp result;

	/**
	 * Gets the value of the cord property.
	 * 
	 * @return possible object is {@link WsCoordinate }
	 * 
	 */
	public WsCoordinate getCord() {
		return cord;
	}

	/**
	 * Sets the value of the cord property.
	 * 
	 * @param value
	 *            allowed object is {@link WsCoordinate }
	 * 
	 */
	public void setCord(WsCoordinate value) {
		this.cord = value;
	}

	/**
	 * Gets the value of the result property.
	 * 
	 * @return possible object is {@link CommonResp }
	 * 
	 */
	public CommonResp getResult() {
		return result;
	}

	/**
	 * Sets the value of the result property.
	 * 
	 * @param value
	 *            allowed object is {@link CommonResp }
	 * 
	 */
	public void setResult(CommonResp value) {
		this.result = value;
	}

	@Override
	public String toString() {
		return "GetSpaceShuttlePosResponse [cord=" + cord + ", result=" + result + "]";
	}

}
