package hu.unideb.inf;

import java.util.List;

import eu.loxon.centralcontrol.CentralControl;
import eu.loxon.centralcontrol.GetSpaceShuttleExitPosResponse;
import eu.loxon.centralcontrol.GetSpaceShuttlePosResponse;
import eu.loxon.centralcontrol.MoveBuilderUnitRequest;
import eu.loxon.centralcontrol.MoveBuilderUnitResponse;
import eu.loxon.centralcontrol.ObjectFactory;
import eu.loxon.centralcontrol.RadarRequest;
import eu.loxon.centralcontrol.RadarResponse;
import eu.loxon.centralcontrol.Scouting;
import eu.loxon.centralcontrol.StartGameResponse;
import eu.loxon.centralcontrol.StructureTunnelRequest;
import eu.loxon.centralcontrol.StructureTunnelResponse;
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

		StructureTunnelRequest structureTunnelRequest = new StructureTunnelRequest();
		structureTunnelRequest.setUnit(0);
		structureTunnelRequest.setDirection(WsDirection.RIGHT);
		StructureTunnelResponse structureTunnelResponse = centralControl.structureTunnel(structureTunnelRequest);
		System.out.println(structureTunnelResponse);
		System.out.println();

		MoveBuilderUnitRequest moveBuilderUnitRequest = new MoveBuilderUnitRequest();
		moveBuilderUnitRequest.setUnit(0);
		moveBuilderUnitRequest.setDirection(WsDirection.RIGHT);
		MoveBuilderUnitResponse moveBuilderUnitResponse = centralControl.moveBuilderUnit(moveBuilderUnitRequest);
		System.out.println(moveBuilderUnitResponse);
		System.out.println();

		RadarRequest radarRequest = objectFactory.createRadarRequest();
		radarRequest.setUnit(0);
		RadarResponse radarResponse = centralControl.radar(radarRequest);
		List<Scouting> scoutingList = radarResponse.getScout();
		for (Scouting scouting : scoutingList) {
			System.out.println(scouting);
		}

		System.out.println();
		System.out.println(radarResponse.getResult());
	}

}
