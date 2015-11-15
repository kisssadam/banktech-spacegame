package hu.unideb.inf;

import java.util.List;

import eu.loxon.centralcontrol.CentralControl;
import eu.loxon.centralcontrol.GetSpaceShuttleExitPosResponse;
import eu.loxon.centralcontrol.GetSpaceShuttlePosResponse;
import eu.loxon.centralcontrol.IsMyTurnRequest;
import eu.loxon.centralcontrol.IsMyTurnResponse;
import eu.loxon.centralcontrol.MoveBuilderUnitRequest;
import eu.loxon.centralcontrol.MoveBuilderUnitResponse;
import eu.loxon.centralcontrol.ObjectFactory;
import eu.loxon.centralcontrol.RadarRequest;
import eu.loxon.centralcontrol.RadarResponse;
import eu.loxon.centralcontrol.Scouting;
import eu.loxon.centralcontrol.StartGameResponse;
import eu.loxon.centralcontrol.StructureTunnelRequest;
import eu.loxon.centralcontrol.StructureTunnelResponse;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;

public class GameController {

	private static ObjectFactory objectFactory = new ObjectFactory();
	private CentralControl centralControl;

	public GameController(CentralControl centralControl) {
		this.centralControl = centralControl;
	}

	public void playGame() {
		StartGameResponse startGameResponse = startGame();
		GetSpaceShuttlePosResponse getSpaceShuttlePosResponse = getSpaceShuttlePos();
		GetSpaceShuttleExitPosResponse getSpaceShuttleExitPosResponse = getSpaceShuttleExitPos();

		LandingZone landingZone = new LandingZone(startGameResponse, getSpaceShuttlePosResponse,
				getSpaceShuttleExitPosResponse);

		System.out.println(landingZone);
		System.out.println();
		System.out.println(getSpaceShuttleExitPosResponse.getResult());
		System.out.println();

		waitForMyTurn();

		WsDirection exitDirection = determineExitDirection(landingZone.getSpaceShuttlePos(),
				landingZone.getSpaceShuttleExitPos());
		StructureTunnelResponse structureTunnelResponse = structureTunnel(0, exitDirection);
		System.out.println(structureTunnelResponse);
		System.out.println();

		waitForMyTurn();

		MoveBuilderUnitResponse moveBuilderUnitResponse = moveBuilderUnit(1, exitDirection);
		System.out.println(moveBuilderUnitResponse);
		System.out.println();

		// waitForMyTurn();
		//
		// MoveBuilderUnitRequest moveBuilderUnitRequest1 = new MoveBuilderUnitRequest();
		// moveBuilderUnitRequest1.setUnit(1);
		// moveBuilderUnitRequest1.setDirection(exitDirection);
		// MoveBuilderUnitResponse moveBuilderUnitResponse1 = centralControl.moveBuilderUnit(moveBuilderUnitRequest1);
		// System.out.println(moveBuilderUnitResponse1);
		// System.out.println();

		// waitForMyTurn();
		//
		// MoveBuilderUnitRequest moveBuilderUnitRequest2 = new MoveBuilderUnitRequest();
		// moveBuilderUnitRequest2.setUnit(2);
		// moveBuilderUnitRequest2.setDirection(exitDirection);
		// MoveBuilderUnitResponse moveBuilderUnitResponse2 = centralControl.moveBuilderUnit(moveBuilderUnitRequest2);
		// System.out.println(moveBuilderUnitResponse2);
		// System.out.println();

		// RadarRequest radarRequest = objectFactory.createRadarRequest();
		// radarRequest.setUnit(0);
		// radarRequest.getCord().add(new WsCoordinate(4, 16));
		// radarRequest.getCord().add(new WsCoordinate(3, 16));
		// // radarRequest.getCord().add(new WsCoordinate(2, 16));
		// radarRequest.getCord().add(new WsCoordinate(5, 16));
		// radarRequest.getCord().add(new WsCoordinate(6, 16));
		//
		// RadarResponse radarResponse = centralControl.radar(radarRequest);
		// List<Scouting> scoutingList = radarResponse.getScout();
		// System.out.println("Radar");
		// for (Scouting scouting : scoutingList) {
		// System.out.println(scouting);
		// }
		//
		// System.out.println();
		// System.out.println(radarResponse.getResult());
	}

	private MoveBuilderUnitResponse moveBuilderUnit(int unit, WsDirection wsDirection) {
		MoveBuilderUnitRequest moveBuilderUnitRequest = new MoveBuilderUnitRequest();

		moveBuilderUnitRequest.setUnit(unit);
		moveBuilderUnitRequest.setDirection(wsDirection);

		return centralControl.moveBuilderUnit(moveBuilderUnitRequest);
	}

	private StartGameResponse startGame() {
		return centralControl.startGame(objectFactory.createStartGameRequest());
	}

	private GetSpaceShuttlePosResponse getSpaceShuttlePos() {
		return centralControl.getSpaceShuttlePos(objectFactory.createGetSpaceShuttlePosRequest());
	}

	private GetSpaceShuttleExitPosResponse getSpaceShuttleExitPos() {
		return centralControl.getSpaceShuttleExitPos(objectFactory.createGetSpaceShuttleExitPosRequest());
	}

	private StructureTunnelResponse structureTunnel(int unit, WsDirection wsDirection) {
		StructureTunnelRequest structureTunnelRequest = new StructureTunnelRequest();

		structureTunnelRequest.setUnit(unit);
		structureTunnelRequest.setDirection(wsDirection);

		return centralControl.structureTunnel(structureTunnelRequest);
	}

	private long lastIsMyTurnRequest = 0L;

	private void waitForMyTurn() {
		do {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while ((System.currentTimeMillis() - this.lastIsMyTurnRequest) < 350);

		boolean isMyTurn = false;
		do {
			IsMyTurnRequest isMyTurnRequest = new IsMyTurnRequest();
			IsMyTurnResponse isMyTurnResponse = centralControl.isMyTurn(isMyTurnRequest);
			System.out.println(isMyTurnResponse);

			isMyTurn = isMyTurnResponse.isIsYourTurn();
			if (isMyTurn) {
				this.lastIsMyTurnRequest = System.currentTimeMillis();
				break;
			} else {
				try {
					Thread.sleep(350);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while (!isMyTurn);
	}

	private WsDirection determineExitDirection(WsCoordinate spaceShuttlePos, WsCoordinate spaceShuttleExitPos) {
		int diffX = spaceShuttleExitPos.getX() - spaceShuttlePos.getX();
		int diffY = spaceShuttleExitPos.getY() - spaceShuttlePos.getY();
		if (diffX > 0) {
			return WsDirection.RIGHT;
		} else if (diffX < 0) {
			return WsDirection.LEFT;
		} else if (diffY > 0) {
			return WsDirection.UP;
		} else {
			return WsDirection.DOWN;
		}
	}
}
