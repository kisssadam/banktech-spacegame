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
		StartGameResponse startGameResponse = centralControl.startGame(objectFactory.createStartGameRequest());
		GetSpaceShuttlePosResponse getSpaceShuttlePosResponse = centralControl
				.getSpaceShuttlePos(objectFactory.createGetSpaceShuttlePosRequest());
		GetSpaceShuttleExitPosResponse getSpaceShuttleExitPosResponse = centralControl
				.getSpaceShuttleExitPos(objectFactory.createGetSpaceShuttleExitPosRequest());

		LandingZone landingZone = new LandingZone(startGameResponse, getSpaceShuttlePosResponse,
				getSpaceShuttleExitPosResponse);

		System.out.println(landingZone);
		System.out.println();
		System.out.println(getSpaceShuttleExitPosResponse.getResult());

		waitForMyTurn();

		StructureTunnelRequest structureTunnelRequest = new StructureTunnelRequest();
		structureTunnelRequest.setUnit(0);
		WsDirection exitDirection = determineExitDirection(landingZone.getSpaceShuttlePos(),
				landingZone.getSpaceShuttleExitPos());
		structureTunnelRequest.setDirection(exitDirection);
		StructureTunnelResponse structureTunnelResponse = centralControl.structureTunnel(structureTunnelRequest);
		System.out.println(structureTunnelResponse);
		System.out.println();

		MoveBuilderUnitRequest moveBuilderUnitRequest = new MoveBuilderUnitRequest();
		moveBuilderUnitRequest.setUnit(0);
		moveBuilderUnitRequest.setDirection(exitDirection);
		MoveBuilderUnitResponse moveBuilderUnitResponse = centralControl.moveBuilderUnit(moveBuilderUnitRequest);
		System.out.println(moveBuilderUnitResponse);
		System.out.println();

		RadarRequest radarRequest = objectFactory.createRadarRequest();
		radarRequest.setUnit(0);
		radarRequest.getCord().add(new WsCoordinate(4, 16));
		radarRequest.getCord().add(new WsCoordinate(3, 16));
		// radarRequest.getCord().add(new WsCoordinate(2, 16));
		radarRequest.getCord().add(new WsCoordinate(5, 16));
		radarRequest.getCord().add(new WsCoordinate(6, 16));

		RadarResponse radarResponse = centralControl.radar(radarRequest);
		List<Scouting> scoutingList = radarResponse.getScout();
		System.out.println("Radar");
		for (Scouting scouting : scoutingList) {
			System.out.println(scouting);
		}

		System.out.println();
		System.out.println(radarResponse.getResult());
	}

	private void waitForMyTurn() {
		boolean isMyTurn = false;
		do {
			IsMyTurnRequest isMyTurnRequest = new IsMyTurnRequest();
			IsMyTurnResponse isMyTurnResponse = centralControl.isMyTurn(isMyTurnRequest);
			isMyTurn = isMyTurnResponse.isIsYourTurn();
			if (isMyTurn) {
				break;
			} else {
				try {
					Thread.sleep(300);
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
