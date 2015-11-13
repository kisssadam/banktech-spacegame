package hu.unideb.inf;

import java.util.Arrays;

import eu.loxon.centralcontrol.GetSpaceShuttleExitPosResponse;
import eu.loxon.centralcontrol.GetSpaceShuttlePosResponse;
import eu.loxon.centralcontrol.ObjectType;
import eu.loxon.centralcontrol.StartGameResponse;
import eu.loxon.centralcontrol.WsCoordinate;

public class LandingZone {

	private char[][] landingZone;
	private WsCoordinate size;
	private WsCoordinate spaceShuttlePos;
	private WsCoordinate spaceShuttleExitPos;

	public LandingZone(StartGameResponse startGameResponse, GetSpaceShuttlePosResponse getSpaceShuttlePosResponse,
			GetSpaceShuttleExitPosResponse getSpaceShuttleExitPosResponse) {
		this.size = startGameResponse.getSize();
		this.spaceShuttlePos = getSpaceShuttlePosResponse.getCord();
		this.spaceShuttleExitPos = getSpaceShuttleExitPosResponse.getCord();

		initLandingZone();
	}

	private final void initLandingZone() {
		landingZone = new char[size.getX() + 1][size.getY() + 1];

		for (int i = 0; i < landingZone.length; i++) {
			Arrays.fill(landingZone[i], ObjectType.UNINITIALIZED.firstChar());
		}

		landingZone[spaceShuttlePos.getX()][spaceShuttlePos.getY()] = ObjectType.SHUTTLE.firstChar();

		// A kijárati cella kristályos szerkezetű.
		landingZone[spaceShuttleExitPos.getX()][spaceShuttleExitPos.getY()] = ObjectType.ROCK.firstChar();
	}

	public WsCoordinate getSpaceShuttlePos() {
		return spaceShuttlePos;
	}

	public WsCoordinate getSpaceShuttleExitPos() {
		return spaceShuttleExitPos;
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

		stringBuilder.append("Size of landing zone: ").append(size);
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("spaceShuttlePos: ").append(spaceShuttlePos);
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("spaceShuttleExitPos: ").append(spaceShuttleExitPos);

		return stringBuilder.toString();
	}

}
