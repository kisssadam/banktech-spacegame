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
 *         &lt;element name="size" type="{http://www.loxon.eu/CentralControl/}wsCoordinate"/>
 *         &lt;element name="result" type="{http://www.loxon.eu/CentralControl/}commonResp"/>
 *         &lt;element name="units" type="{http://www.loxon.eu/CentralControl/}wsBuilderunit" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "size", "result", "units" })
@XmlRootElement(name = "startGameResponse")
public class StartGameResponse {

	@XmlElement(required = true)
	protected WsCoordinate size;
	@XmlElement(required = true)
	protected CommonResp result;
	@XmlElement(required = true)
	protected List<WsBuilderunit> units;

	/**
	 * Gets the value of the size property.
	 * 
	 * @return possible object is {@link WsCoordinate }
	 * 
	 */
	public WsCoordinate getSize() {
		return size;
	}

	/**
	 * Sets the value of the size property.
	 * 
	 * @param value
	 *            allowed object is {@link WsCoordinate }
	 * 
	 */
	public void setSize(WsCoordinate value) {
		this.size = value;
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

	/**
	 * Gets the value of the units property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the units property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getUnits().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link WsBuilderunit }
	 * 
	 * 
	 */
	public List<WsBuilderunit> getUnits() {
		if (units == null) {
			units = new ArrayList<WsBuilderunit>();
		}
		return this.units;
	}

	@Override
	public String toString() {
		return "StartGameResponse [size=" + size + ", result=" + result + ", units=" + units + "]";
	}

}
