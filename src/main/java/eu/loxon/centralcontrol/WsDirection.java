package eu.loxon.centralcontrol;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for wsDirection.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="wsDirection">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="UP"/>
 *     &lt;enumeration value="DOWN"/>
 *     &lt;enumeration value="LEFT"/>
 *     &lt;enumeration value="RIGHT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "wsDirection")
@XmlEnum
public enum WsDirection {

	UP, DOWN, LEFT, RIGHT;

	public String value() {
		return name();
	}

	public static WsDirection fromValue(String v) {
		return valueOf(v);
	}

}
