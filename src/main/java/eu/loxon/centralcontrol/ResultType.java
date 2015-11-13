package eu.loxon.centralcontrol;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for resultType.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="resultType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DONE"/>
 *     &lt;enumeration value="INVALID"/>
 *     &lt;enumeration value="ERROR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "resultType")
@XmlEnum
public enum ResultType {

	/**
	 * A kérés sikeresen végrehajtódott.
	 */
	DONE,

	/**
	 * A kérés végrehajtása sikertelen. Részletek az üzenetben.
	 */
	INVALID,

	/**
	 * Hiba történt a központi vezérlésben.
	 */
	ERROR;

	public String value() {
		return name();
	}

	public static ResultType fromValue(String v) {
		return valueOf(v);
	}

}
