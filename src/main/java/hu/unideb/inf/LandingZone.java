package hu.unideb.inf;

import java.util.Arrays;
import java.util.List;

import eu.loxon.centralcontrol.GetSpaceShuttleExitPosResponse;
import eu.loxon.centralcontrol.GetSpaceShuttlePosResponse;
import eu.loxon.centralcontrol.ObjectType;
import eu.loxon.centralcontrol.StartGameResponse;
import eu.loxon.centralcontrol.WsBuilderunit;
import eu.loxon.centralcontrol.WsCoordinate;

public class LandingZone {

	private WsCoordinate size;
	private WsCoordinate[] unitPosition;
	private List<WsBuilderunit> units;
	private WsCoordinate spaceShuttlePos;
	private WsCoordinate spaceShuttleExitPos;
	private char[][] landingZone;

	public LandingZone(StartGameResponse startGameResponse, GetSpaceShuttlePosResponse getSpaceShuttlePosResponse,
			GetSpaceShuttleExitPosResponse getSpaceShuttleExitPosResponse) {
		this.size = startGameResponse.getSize();
		this.units = startGameResponse.getUnits();

		this.spaceShuttlePos = getSpaceShuttlePosResponse.getCord();
		this.spaceShuttleExitPos = getSpaceShuttleExitPosResponse.getCord();

		this.unitPosition = new WsCoordinate[4];
		for (int i = 0; i < unitPosition.length; i++) {
			unitPosition[i] = this.spaceShuttlePos;
		}

		initLandingZone();
	}

	private final void initLandingZone() {
		landingZone = new char[size.getX() + 1][size.getY() + 1];

		for (int i = 0; i < landingZone.length; i++) {
			Arrays.fill(landingZone[i], ObjectType.UNINITIALIZED.firstChar());
		}

		landingZone[spaceShuttlePos.getX()][spaceShuttlePos.getY()] = ObjectType.SHUTTLE.firstChar();

		// A feladat specifikációjából tudjuk, hogy a kijárati cella kristályos szerkezetű.
		landingZone[spaceShuttleExitPos.getX()][spaceShuttleExitPos.getY()] = ObjectType.ROCK.firstChar();
	}

	public void set(int unit, WsCoordinate wsCoordinate) {
		this.unitPosition[unit] = wsCoordinate;
	}

	public WsCoordinate[] getUnitPosition() {
		return unitPosition;
	}

	public void setUnitPosition(WsCoordinate[] unitPosition) {
		this.unitPosition = unitPosition;
	}

	public WsCoordinate getSpaceShuttlePos() {
		return spaceShuttlePos;
	}

	public WsCoordinate getSpaceShuttleExitPos() {
		return spaceShuttleExitPos;
	}

	public List<WsBuilderunit> getUnits() {
		return units;
	}

	public void setUnits(List<WsBuilderunit> units) {
		this.units = units;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();

		for (int j = landingZone[0].length - 1; j > 0; j--) {
			for (int i = 1; i < landingZone.length; i++) {
				stringBuilder.append(landingZone[i][j]);
				stringBuilder.append(i + 1 < landingZone.length ? " " : System.lineSeparator());
			}
		}
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("Builder units: ").append(units.toString());
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("Size of landing zone: ").append(size);
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("spaceShuttlePos: ").append(spaceShuttlePos);
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("spaceShuttleExitPos: ").append(spaceShuttleExitPos);

		return stringBuilder.toString();
	}

}
