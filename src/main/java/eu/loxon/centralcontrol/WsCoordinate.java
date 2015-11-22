package eu.loxon.centralcontrol;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for wsCoordinate complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wsCoordinate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="x" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="y" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsCoordinate", propOrder = { "x", "y" })
public class WsCoordinate {

	protected int x;
	protected int y;

	public WsCoordinate(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public WsCoordinate() {
		super();
	}

	/**
	 * Gets the value of the x property.
	 * 
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the value of the x property.
	 * 
	 */
	public void setX(int value) {
		this.x = value;
	}

	/**
	 * Gets the value of the y property.
	 * 
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the value of the y property.
	 * 
	 */
	public void setY(int value) {
		this.y = value;
	}

	public List<WsCoordinate> getNeightborCoordinates() {
		List<WsCoordinate> neighbors = new ArrayList<>(4);

		neighbors.add(new WsCoordinate(this.x + 1, y));
		neighbors.add(new WsCoordinate(this.x - 1, y));
		neighbors.add(new WsCoordinate(this.x, y + 1));
		neighbors.add(new WsCoordinate(this.x, y - 1));

		return neighbors;
	}

	@Override
	public String toString() {
		return "WsCoordinate [x=" + x + ", y=" + y + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 641;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WsCoordinate other = (WsCoordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
