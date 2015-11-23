package eu.loxon.centralcontrol;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import hu.unideb.inf.LandingZonePart;

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

	public WsCoordinate[] getNeighborCoordinates() {
		return new WsCoordinate[] {
				/*
				 * LEFT NEIGHBOR
				 */
				new WsCoordinate(x - 1, y),

				/*
				 * UP NEIGHBOR
				 */
				new WsCoordinate(x, y + 1),

				/*
				 * RIGHT NEIGHBOR
				 */

				new WsCoordinate(x + 1, y),
				/*
				 * DOWN NEIGHBOR
				 */
				new WsCoordinate(x, y - 1)

		};
	}

	// public WsCoordinate[] getNeighborCoordinates(LandingZonePart shuttleLandingZonePart) {
	// WsCoordinate[] neighbors;
	//
	// switch (shuttleLandingZonePart) {
	// case BOTTOM_LEFT:
	// neighbors = new WsCoordinate[] {
	// /*
	// * UP NEIGHBOR
	// */
	// new WsCoordinate(x, y + 1),
	//
	// /*
	// * RIGHT NEIGHBOR
	// */
	//
	// new WsCoordinate(x + 1, y),
	// /*
	// * DOWN NEIGHBOR
	// */
	// new WsCoordinate(x, y - 1),
	//
	// /*
	// * LEFT NEIGHBOR
	// */
	// new WsCoordinate(x - 1, y) };
	// break;
	//
	// case BOTTOM_RIGHT:
	// neighbors = new WsCoordinate[] {
	// /*
	// * LEFT NEIGHBOR
	// */
	// new WsCoordinate(x - 1, y),
	//
	// /*
	// * UP NEIGHBOR
	// */
	// new WsCoordinate(x, y + 1),
	//
	// /*
	// * RIGHT NEIGHBOR
	// */
	// new WsCoordinate(x + 1, y),
	//
	// /*
	// * DOWN NEIGHBOR
	// */
	// new WsCoordinate(x, y - 1) };
	// break;
	//
	// case CENTER:
	// default:
	// neighbors = new WsCoordinate[] {
	// /*
	// * LEFT NEIGHBOR
	// */
	// new WsCoordinate(x - 1, y),
	//
	// /*
	// * UP NEIGHBOR
	// */
	// new WsCoordinate(x, y + 1),
	//
	// /*
	// * RIGHT NEIGHBOR
	// */
	// new WsCoordinate(x + 1, y),
	//
	// /*
	// * DOWN NEIGHBOR
	// */
	// new WsCoordinate(x, y - 1) };
	// break;
	//
	// case TOP_LEFT:
	// neighbors = new WsCoordinate[] {
	// /*
	// * DOWN NEIGHBOR
	// */
	// new WsCoordinate(x, y - 1),
	//
	// /*
	// * RIGHT NEIGHBOR
	// */
	// new WsCoordinate(x + 1, y),
	//
	// /*
	// * UP NEIGHBOR
	// */
	// new WsCoordinate(x, y + 1),
	//
	// /*
	// * LEFT NEIGHBOR
	// */
	// new WsCoordinate(x - 1, y) };
	// break;
	//
	// case TOP_RIGHT:
	// neighbors = new WsCoordinate[] {
	// /*
	// * LEFT NEIGHBOR
	// */
	// new WsCoordinate(x - 1, y),
	//
	// /*
	// * DOWN NEIGHBOR
	// */
	// new WsCoordinate(x, y - 1),
	//
	// /*
	// * RIGHT NEIGHBOR
	// */
	// new WsCoordinate(x + 1, y),
	//
	// /*
	// * UP NEIGHBOR
	// */
	// new WsCoordinate(x, y + 1) };
	// break;
	// }
	// return neighbors;
	// }
	//
	// public WsCoordinate[] getSecondNeightborCoordinates() {
	// return new WsCoordinate[] {
	// /*
	// * SECOND LEFT NEIGHBOR
	// */
	// new WsCoordinate(x - 2, y),
	//
	// /*
	// * SECOND RIGHT NEIGHBOR
	// */
	// new WsCoordinate(x + 2, y),
	//
	// /*
	// * SECOND UP NEIGHBOR
	// */
	// new WsCoordinate(x, y + 2),
	//
	// /*
	// * SECOND DOWN NEIGHBOR
	// */
	// new WsCoordinate(x, y - 2) };
	// }

	public List<WsCoordinate> getThirdNeightborCoordinates() {
		List<WsCoordinate> thirdNeighborCoordinates = new ArrayList<>(4);

		thirdNeighborCoordinates.add(new WsCoordinate(x - 3, y));
		thirdNeighborCoordinates.add(new WsCoordinate(x + 3, y));
		thirdNeighborCoordinates.add(new WsCoordinate(x, y - 3));
		thirdNeighborCoordinates.add(new WsCoordinate(x, y + 3));

		return thirdNeighborCoordinates;
	}

	public int distanceFrom(WsCoordinate coordinate) {
		return Math.abs(this.x - coordinate.x) + Math.abs(this.y - coordinate.y);
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
