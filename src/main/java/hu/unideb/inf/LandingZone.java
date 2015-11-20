package hu.unideb.inf;

import java.util.Arrays;
import java.util.List;

import eu.loxon.centralcontrol.GetSpaceShuttleExitPosResponse;
import eu.loxon.centralcontrol.GetSpaceShuttlePosResponse;
import eu.loxon.centralcontrol.ObjectType;
import eu.loxon.centralcontrol.Scouting;
import eu.loxon.centralcontrol.StartGameResponse;
import eu.loxon.centralcontrol.WsBuilderunit;
import eu.loxon.centralcontrol.WsCoordinate;

public class LandingZone {

	private WsCoordinate size;
	private WsCoordinate[] unitPosition;
	private List<WsBuilderunit> units;
	private WsCoordinate spaceShuttlePos;
	private WsCoordinate spaceShuttleExitPos;
	private ObjectType[][] terrain;
	private String[][] ownerTeam;

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
		terrain = new ObjectType[size.getX() + 1][size.getY() + 1];
		ownerTeam = new String[size.getX() + 1][size.getY() + 1];

		for (int i = 0; i < terrain.length; i++) {
			Arrays.fill(terrain[i], ObjectType.UNINITIALIZED);
			Arrays.fill(ownerTeam[i], "");
		}

		for (int i = 0; i < terrain.length; i++) {
			terrain[i][0] = ObjectType.OBSIDIAN;
			terrain[i][size.getY()] = ObjectType.OBSIDIAN;
		}

		for (int i = 0; i < terrain[0].length; i++) {
			terrain[0][i] = ObjectType.OBSIDIAN;
			terrain[size.getX()][i] = ObjectType.OBSIDIAN;
		}

		terrain[spaceShuttlePos.getX()][spaceShuttlePos.getY()] = ObjectType.SHUTTLE;
		ownerTeam[spaceShuttlePos.getX()][spaceShuttlePos.getY()] = GameController.TEAM_NAME;

		// A feladat specifikációjából tudjuk, hogy a kijárati cella kristályos szerkezetű.
		terrain[spaceShuttleExitPos.getX()][spaceShuttleExitPos.getY()] = ObjectType.ROCK;
	}

	public void processScoutings(List<Scouting> scoutings) {
		for (Scouting scouting : scoutings) {
			WsCoordinate wsCoordinate = scouting.getCord();
			terrain[wsCoordinate.getX()][wsCoordinate.getY()] = scouting.getObject();
			ownerTeam[wsCoordinate.getX()][wsCoordinate.getY()] = scouting.getTeam();
		}
	}

	public void setTerrain(WsCoordinate coordinate, ObjectType object) {
		System.out.println("Setting terrain on: " + coordinate + " to: " + object);
		terrain[coordinate.getX()][coordinate.getY()] = object;
	}

	public void setUnitPosition(int unit, WsCoordinate wsCoordinate) {
		System.out.println("Setting no. " + unit + " unit position to: " + wsCoordinate);
		this.unitPosition[unit] = wsCoordinate;
	}

	public int determineCentralRadius() {
		return (size.getX() < size.getY() ? size.getX() : size.getY()) / 4;
	}

	public WsCoordinate determineCentralCoordinates() {
		return new WsCoordinate(size.getX() / 2, size.getY() / 2);
	}

	public WsCoordinate[] getUnitPosition() {
		return unitPosition;
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

	public WsCoordinate getSize() {
		return size;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();

		for (int j = terrain[0].length - 1; j >= 0; j--) {
			for (int i = 0; i < terrain.length; i++) {
				stringBuilder.append(terrain[i][j].firstChar());
				stringBuilder.append(i + 1 < terrain.length ? " " : System.lineSeparator());
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
