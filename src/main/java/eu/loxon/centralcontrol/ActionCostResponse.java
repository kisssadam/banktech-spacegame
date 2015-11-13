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
 *         &lt;element name="drill" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="move" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="radar" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="explode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="watch" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="availableActionPoints" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="availableExplosives" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
@XmlType(name = "", propOrder = { "drill", "move", "radar", "explode", "watch", "availableActionPoints",
		"availableExplosives", "result" })
@XmlRootElement(name = "actionCostResponse")
public class ActionCostResponse {

	protected int drill;
	protected int move;
	protected int radar;
	protected int explode;
	protected int watch;
	protected int availableActionPoints;
	protected int availableExplosives;
	@XmlElement(required = true)
	protected CommonResp result;

	/**
	 * Gets the value of the drill property.
	 * 
	 */
	public int getDrill() {
		return drill;
	}

	/**
	 * Sets the value of the drill property.
	 * 
	 */
	public void setDrill(int value) {
		this.drill = value;
	}

	/**
	 * Gets the value of the move property.
	 * 
	 */
	public int getMove() {
		return move;
	}

	/**
	 * Sets the value of the move property.
	 * 
	 */
	public void setMove(int value) {
		this.move = value;
	}

	/**
	 * Gets the value of the radar property.
	 * 
	 */
	public int getRadar() {
		return radar;
	}

	/**
	 * Sets the value of the radar property.
	 * 
	 */
	public void setRadar(int value) {
		this.radar = value;
	}

	/**
	 * Gets the value of the explode property.
	 * 
	 */
	public int getExplode() {
		return explode;
	}

	/**
	 * Sets the value of the explode property.
	 * 
	 */
	public void setExplode(int value) {
		this.explode = value;
	}

	/**
	 * Gets the value of the watch property.
	 * 
	 */
	public int getWatch() {
		return watch;
	}

	/**
	 * Sets the value of the watch property.
	 * 
	 */
	public void setWatch(int value) {
		this.watch = value;
	}

	/**
	 * Gets the value of the availableActionPoints property.
	 * 
	 */
	public int getAvailableActionPoints() {
		return availableActionPoints;
	}

	/**
	 * Sets the value of the availableActionPoints property.
	 * 
	 */
	public void setAvailableActionPoints(int value) {
		this.availableActionPoints = value;
	}

	/**
	 * Gets the value of the availableExplosives property.
	 * 
	 */
	public int getAvailableExplosives() {
		return availableExplosives;
	}

	/**
	 * Sets the value of the availableExplosives property.
	 * 
	 */
	public void setAvailableExplosives(int value) {
		this.availableExplosives = value;
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
		return "ActionCostResponse [drill=" + drill + ", move=" + move + ", radar=" + radar + ", explode=" + explode
				+ ", watch=" + watch + ", availableActionPoints=" + availableActionPoints + ", availableExplosives="
				+ availableExplosives + ", result=" + result + "]";
	}

}
