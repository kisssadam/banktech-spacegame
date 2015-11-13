package eu.loxon.centralcontrol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for scouting complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="scouting">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cord" type="{http://www.loxon.eu/CentralControl/}wsCoordinate"/>
 *         &lt;element name="object" type="{http://www.loxon.eu/CentralControl/}objectType"/>
 *         &lt;element name="team" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scouting", propOrder = { "cord", "object", "team" })
public class Scouting {

	@XmlElement(required = true)
	protected WsCoordinate cord;
	@XmlElement(required = true)
	@XmlSchemaType(name = "string")
	protected ObjectType object;
	@XmlElement(required = true)
	protected String team;

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
	 * Gets the value of the object property.
	 * 
	 * @return possible object is {@link ObjectType }
	 * 
	 */
	public ObjectType getObject() {
		return object;
	}

	/**
	 * Sets the value of the object property.
	 * 
	 * @param value
	 *            allowed object is {@link ObjectType }
	 * 
	 */
	public void setObject(ObjectType value) {
		this.object = value;
	}

	/**
	 * Gets the value of the team property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * Sets the value of the team property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTeam(String value) {
		this.team = value;
	}

	@Override
	public String toString() {
		return "Scouting [cord=" + cord + ", object=" + object + ", team=" + team + "]";
	}

}
