package eu.loxon.centralcontrol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for wsBuilderunit complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wsBuilderunit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cord" type="{http://www.loxon.eu/CentralControl/}wsCoordinate"/>
 *         &lt;element name="unitid" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsBuilderunit", propOrder = { "cord", "unitid" })
public class WsBuilderunit {

	@XmlElement(required = true)
	protected WsCoordinate cord;
	protected int unitid;

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
	 * Gets the value of the unitid property.
	 * 
	 */
	public int getUnitid() {
		return unitid;
	}

	/**
	 * Sets the value of the unitid property.
	 * 
	 */
	public void setUnitid(int value) {
		this.unitid = value;
	}

	@Override
	public String toString() {
		return "WsBuilderunit [cord=" + cord + ", unitid=" + unitid + "]";
	}

}
