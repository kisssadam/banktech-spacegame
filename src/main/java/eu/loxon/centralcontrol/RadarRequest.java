package eu.loxon.centralcontrol;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="unit" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="cord" type="{http://www.loxon.eu/CentralControl/}wsCoordinate" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "unit", "cord" })
@XmlRootElement(name = "radarRequest")
public class RadarRequest {

	protected int unit;
	@XmlElement(required = true)
	protected List<WsCoordinate> cord;

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
	 * Gets the value of the cord property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the cord property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getCord().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link WsCoordinate }
	 * 
	 * 
	 */
	public List<WsCoordinate> getCord() {
		if (cord == null) {
			cord = new ArrayList<WsCoordinate>();
		}
		return this.cord;
	}

}
