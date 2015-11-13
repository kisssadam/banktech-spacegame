package eu.loxon.centralcontrol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for wsScore complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wsScore">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="reward" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="bonus" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="penalty" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="total" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsScore", propOrder = { "reward", "bonus", "penalty", "total" })
public class WsScore {

	protected long reward;
	protected long bonus;
	protected long penalty;
	protected long total;

	/**
	 * Gets the value of the reward property.
	 * 
	 */
	public long getReward() {
		return reward;
	}

	/**
	 * Sets the value of the reward property.
	 * 
	 */
	public void setReward(long value) {
		this.reward = value;
	}

	/**
	 * Gets the value of the bonus property.
	 * 
	 */
	public long getBonus() {
		return bonus;
	}

	/**
	 * Sets the value of the bonus property.
	 * 
	 */
	public void setBonus(long value) {
		this.bonus = value;
	}

	/**
	 * Gets the value of the penalty property.
	 * 
	 */
	public long getPenalty() {
		return penalty;
	}

	/**
	 * Sets the value of the penalty property.
	 * 
	 */
	public void setPenalty(long value) {
		this.penalty = value;
	}

	/**
	 * Gets the value of the total property.
	 * 
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * Sets the value of the total property.
	 * 
	 */
	public void setTotal(long value) {
		this.total = value;
	}

	@Override
	public String toString() {
		return "WsScore [reward=" + reward + ", bonus=" + bonus + ", penalty=" + penalty + ", total=" + total + "]";
	}

}
