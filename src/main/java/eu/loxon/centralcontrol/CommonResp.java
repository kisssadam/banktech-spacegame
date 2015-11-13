package eu.loxon.centralcontrol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for commonResp complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="commonResp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="actionPointsLeft" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="explosivesLeft" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="turnsLeft" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="type" type="{http://www.loxon.eu/CentralControl/}resultType"/>
 *         &lt;element name="builderUnit" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="score" type="{http://www.loxon.eu/CentralControl/}wsScore"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "commonResp", propOrder = { "actionPointsLeft", "explosivesLeft", "turnsLeft", "type", "builderUnit",
		"message", "code", "score" })
public class CommonResp {

	protected int actionPointsLeft;
	protected int explosivesLeft;
	protected int turnsLeft;
	@XmlElement(required = true)
	@XmlSchemaType(name = "string")
	protected ResultType type;
	protected int builderUnit;
	@XmlElement(required = true)
	protected String message;
	@XmlElement(required = true)
	protected String code;
	@XmlElement(required = true)
	protected WsScore score;

	/**
	 * Gets the value of the actionPointsLeft property.
	 * 
	 */
	public int getActionPointsLeft() {
		return actionPointsLeft;
	}

	/**
	 * Sets the value of the actionPointsLeft property.
	 * 
	 */
	public void setActionPointsLeft(int value) {
		this.actionPointsLeft = value;
	}

	/**
	 * Gets the value of the explosivesLeft property.
	 * 
	 */
	public int getExplosivesLeft() {
		return explosivesLeft;
	}

	/**
	 * Sets the value of the explosivesLeft property.
	 * 
	 */
	public void setExplosivesLeft(int value) {
		this.explosivesLeft = value;
	}

	/**
	 * Gets the value of the turnsLeft property.
	 * 
	 */
	public int getTurnsLeft() {
		return turnsLeft;
	}

	/**
	 * Sets the value of the turnsLeft property.
	 * 
	 */
	public void setTurnsLeft(int value) {
		this.turnsLeft = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link ResultType }
	 * 
	 */
	public ResultType getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link ResultType }
	 * 
	 */
	public void setType(ResultType value) {
		this.type = value;
	}

	/**
	 * Gets the value of the builderUnit property.
	 * 
	 */
	public int getBuilderUnit() {
		return builderUnit;
	}

	/**
	 * Sets the value of the builderUnit property.
	 * 
	 */
	public void setBuilderUnit(int value) {
		this.builderUnit = value;
	}

	/**
	 * Gets the value of the message property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the value of the message property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMessage(String value) {
		this.message = value;
	}

	/**
	 * Gets the value of the code property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the value of the code property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCode(String value) {
		this.code = value;
	}

	/**
	 * Gets the value of the score property.
	 * 
	 * @return possible object is {@link WsScore }
	 * 
	 */
	public WsScore getScore() {
		return score;
	}

	/**
	 * Sets the value of the score property.
	 * 
	 * @param value
	 *            allowed object is {@link WsScore }
	 * 
	 */
	public void setScore(WsScore value) {
		this.score = value;
	}

	@Override
	public String toString() {
		return "CommonResp [actionPointsLeft=" + actionPointsLeft + ", explosivesLeft=" + explosivesLeft
				+ ", turnsLeft=" + turnsLeft + ", type=" + type + ", builderUnit=" + builderUnit + ", message="
				+ message + ", code=" + code + ", score=" + score + "]";
	}

}
