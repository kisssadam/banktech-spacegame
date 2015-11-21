package eu.loxon.centralcontrol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="unit" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="direction" type="{http://www.loxon.eu/CentralControl/}wsDirection"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "unit", "direction" })
@XmlRootElement(name = "structureTunnelRequest")
public class StructureTunnelRequest {

	protected int unit;
	@XmlElement(required = true)
	@XmlSchemaType(name = "string")
	protected WsDirection direction;

	/**
	 * Gets the value of the unit property.
	 * 
	 */
	public int getUnit() {
		return unit;
	}

	/**
	 * Sets the value of the unit property.
	 * 
	 */
	public void setUnit(int value) {
		this.unit = value;
	}

	/**
	 * Gets the value of the direction property.
	 * 
	 * @return possible object is {@link WsDirection }
	 * 
	 */
	public WsDirection getDirection() {
		return direction;
	}

	/**
	 * Sets the value of the direction property.
	 * 
	 * @param value
	 *            allowed object is {@link WsDirection }
	 * 
	 */
	public void setDirection(WsDirection value) {
		this.direction = value;
	}

	@Override
	public String toString() {
		return "StructureTunnelRequest [unit=" + unit + ", direction=" + direction + "]";
	}

}
