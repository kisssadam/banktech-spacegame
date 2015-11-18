package hu.unideb.inf;

import java.util.List;

import eu.loxon.centralcontrol.ActionCostResponse;
import eu.loxon.centralcontrol.CentralControl;
import eu.loxon.centralcontrol.CommonResp;
import eu.loxon.centralcontrol.ExplodeCellRequest;
import eu.loxon.centralcontrol.ExplodeCellResponse;
import eu.loxon.centralcontrol.GetSpaceShuttleExitPosResponse;
import eu.loxon.centralcontrol.GetSpaceShuttlePosResponse;
import eu.loxon.centralcontrol.IsMyTurnRequest;
import eu.loxon.centralcontrol.IsMyTurnResponse;
import eu.loxon.centralcontrol.MoveBuilderUnitRequest;
import eu.loxon.centralcontrol.MoveBuilderUnitResponse;
import eu.loxon.centralcontrol.ObjectFactory;
import eu.loxon.centralcontrol.ObjectType;
import eu.loxon.centralcontrol.RadarRequest;
import eu.loxon.centralcontrol.RadarResponse;
import eu.loxon.centralcontrol.ResultType;
import eu.loxon.centralcontrol.Scouting;
import eu.loxon.centralcontrol.StartGameResponse;
import eu.loxon.centralcontrol.StructureTunnelRequest;
import eu.loxon.centralcontrol.StructureTunnelResponse;
import eu.loxon.centralcontrol.WatchRequest;
import eu.loxon.centralcontrol.WatchResponse;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;

public class GameController {

	public static final String TEAM_NAME = "0x70unideb";

	private static ObjectFactory objectFactory = new ObjectFactory();
	private CentralControl centralControl;
	private LandingZone landingZone;
	private long lastIsMyTurnRequest = 0L;
	private int drillCost;
	private int explodeCost;
	private int moveCost;
	private int actualBuilderUnit;

	public GameController(CentralControl centralControl) {
		this.centralControl = centralControl;
	}

	public void playGame() throws InterruptedException {
		StartGameResponse startGameResponse = startGame();
		GetSpaceShuttlePosResponse getSpaceShuttlePosResponse = getSpaceShuttlePos();
		GetSpaceShuttleExitPosResponse getSpaceShuttleExitPosResponse = getSpaceShuttleExitPos();

		this.landingZone = new LandingZone(startGameResponse, getSpaceShuttlePosResponse,
				getSpaceShuttleExitPosResponse);
		
		System.out.println(getActionCost());
		WsDirection exitDirection = determineExitDirection(landingZone.getSpaceShuttlePos(),
				landingZone.getSpaceShuttleExitPos());
		System.out.println(watch(actualBuilderUnit));
		System.out.println(landingZone);
		StructureTunnelResponse structureTunnelResponse = structureTunnel(actualBuilderUnit, exitDirection);
		moveBuilderUnit(actualBuilderUnit, exitDirection);
		System.out.println(watch(actualBuilderUnit));

		System.out.println(landingZone);
		System.out.println();
		System.out.println(getSpaceShuttleExitPosResponse.getResult());
		System.out.println();

	}

	private int rateCell(Scouting scouting) {
		int points = 0;
		switch (scouting.getObject()) {
		case TUNNEL:
			if (!TEAM_NAME.equals(scouting.getTeam())) {
				points = explodeCost;
			} else {
				points = moveCost;
			}
			break;
		case SHUTTLE:
			points = Integer.MAX_VALUE;
			break;
		case BUILDER_UNIT:
			points = Integer.MAX_VALUE;
			break;
		case ROCK:
			points = drillCost;
			break;
		case GRANITE:
			points = explodeCost + drillCost;
			break;
		case OBSIDIAN:
			points = Integer.MAX_VALUE;
			break;
		case UNINITIALIZED:
			break;
		}

		return points;
	}

	private List<Scouting> getSurroundingTerrain(int unit) {
		WatchResponse watch = watch(unit);
		// WsCoordinate wsCoordinate = landingZone.getUnitPosition()[unit];
		List<Scouting> scoutings = watch.getScout();
		return scoutings;
	}

	private WatchResponse watch(int unit) {
		WatchRequest watchRequest = objectFactory.createWatchRequest();

		watchRequest.setUnit(unit);

		WatchResponse response = centralControl.watch(watchRequest);
		this.actualBuilderUnit = response.getResult().getBuilderUnit();
		landingZone.processScoutings(response.getScout());
		return response;
	}

	private RadarResponse radar(int unit, List<WsCoordinate> wsCoordinates) {
		RadarRequest radarRequest = objectFactory.createRadarRequest();

		radarRequest.setUnit(unit);
		radarRequest.getCord().addAll(wsCoordinates);

		RadarResponse response = centralControl.radar(radarRequest);
		this.actualBuilderUnit = response.getResult().getBuilderUnit();
		landingZone.processScoutings(response.getScout());
		return response;
	}

	private MoveBuilderUnitResponse moveBuilderUnit(int unit, WsDirection wsDirection) {
		MoveBuilderUnitRequest moveBuilderUnitRequest = new MoveBuilderUnitRequest();

		moveBuilderUnitRequest.setUnit(unit);
		moveBuilderUnitRequest.setDirection(wsDirection);

		MoveBuilderUnitResponse response = centralControl.moveBuilderUnit(moveBuilderUnitRequest);
		if (response.getResult().getType().equals(ResultType.DONE)) {
			landingZone.setUnitPosition(unit, calculateWsCoordinate(landingZone.getUnitPosition()[unit], wsDirection));
		}
		this.actualBuilderUnit = response.getResult().getBuilderUnit();
		landingZone.setTerrain(calculateWsCoordinate(landingZone.getUnitPosition()[unit], wsDirection), ObjectType.BUILDER_UNIT);
		return response;
	}

	private WsCoordinate calculateWsCoordinate(WsCoordinate start, WsDirection wsDirection) {
		System.out.println("start: " + start);
		switch (wsDirection) {
		case UP:
			return new WsCoordinate(start.getX(), start.getY() + 1);
		case DOWN:
			return new WsCoordinate(start.getX(), start.getY() - 1);
		case LEFT:
			return new WsCoordinate(start.getX() - 1, start.getY());
		case RIGHT:
			return new WsCoordinate(start.getX() + 1, start.getY());
		default:
			return null;
		}
	}

	private void refreshActualBuilderUnit(CommonResp result) {
		this.actualBuilderUnit = result.getBuilderUnit();
		System.out.println("Actual builder unit: " + actualBuilderUnit);
	}

	private StartGameResponse startGame() {
		StartGameResponse response = centralControl.startGame(objectFactory.createStartGameRequest());
		refreshActualBuilderUnit(response.getResult());
		return response;
	}

	private GetSpaceShuttlePosResponse getSpaceShuttlePos() {
		GetSpaceShuttlePosResponse response = centralControl
				.getSpaceShuttlePos(objectFactory.createGetSpaceShuttlePosRequest());
		refreshActualBuilderUnit(response.getResult());
		return response;
	}

	private GetSpaceShuttleExitPosResponse getSpaceShuttleExitPos() {
		GetSpaceShuttleExitPosResponse response = centralControl
				.getSpaceShuttleExitPos(objectFactory.createGetSpaceShuttleExitPosRequest());
		refreshActualBuilderUnit(response.getResult());
		return response;
	}

	private ExplodeCellResponse explodeCell(int unit, WsDirection wsDirection) {
		ExplodeCellRequest explodeCellRequest = objectFactory.createExplodeCellRequest();

		explodeCellRequest.setUnit(unit);
		explodeCellRequest.setDirection(wsDirection);

		ExplodeCellResponse response = centralControl.explodeCell(explodeCellRequest);
		refreshActualBuilderUnit(response.getResult());
		return response;
	}

	// Frissít 3 mezőt, fúrási, robbantási és mozgatási költségek. ez alapján
	// értékelünk cellákat
	private ActionCostResponse getActionCost() {
		ActionCostResponse response = centralControl.getActionCost(objectFactory.createActionCostRequest());
		drillCost = response.getDrill();
		explodeCost = response.getExplode();
		moveCost = response.getMove();
		refreshActualBuilderUnit(response.getResult());
		return response;
	}

	private StructureTunnelResponse structureTunnel(int unit, WsDirection wsDirection) {
		StructureTunnelRequest structureTunnelRequest = new StructureTunnelRequest();

		structureTunnelRequest.setUnit(unit);
		structureTunnelRequest.setDirection(wsDirection);

		StructureTunnelResponse response = centralControl.structureTunnel(structureTunnelRequest);
		if(ResultType.DONE.equals(response.getResult().getType())){
			landingZone.setTerrain(calculateWsCoordinate(landingZone.getUnitPosition()[unit], wsDirection), ObjectType.TUNNEL);
		}
		refreshActualBuilderUnit(response.getResult());
		return response;
	}

	private IsMyTurnResponse waitForMyTurn() {
		try {
			return tryToWaitForMyTurn();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	private IsMyTurnResponse tryToWaitForMyTurn() throws InterruptedException {
		IsMyTurnResponse response = null;

		do {
			Thread.sleep(50);
		} while ((System.currentTimeMillis() - this.lastIsMyTurnRequest) < 300);

		boolean isMyTurn = false;
		do {
			IsMyTurnRequest isMyTurnRequest = new IsMyTurnRequest();
			response = centralControl.isMyTurn(isMyTurnRequest);
			refreshActualBuilderUnit(response.getResult());
			System.out.println(response);

			isMyTurn = response.isIsYourTurn();
			if (isMyTurn) {
				this.lastIsMyTurnRequest = System.currentTimeMillis();
				break;
			} else {
				Thread.sleep(350);
			}
		} while (!isMyTurn);

		return response;
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
// for (int i = 0; i < 20; i++) {
//
// waitForMyTurn();
// System.out.println(watch(waitForMyTurn()));

// try {
// Thread.sleep(3000);
// } catch (InterruptedException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
// }

// try {
// Thread.sleep(3000);
// } catch (InterruptedException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }

// System.out.println(moveBuilderUnit(0, WsDirection.RIGHT));
// System.out.println(watch(0));

// waitForMyTurn();
// System.out.println(watch(1));

// waitForMyTurn();
//
// WsDirection exitDirection =
// determineExitDirection(landingZone.getSpaceShuttlePos(),
// landingZone.getSpaceShuttleExitPos());
// StructureTunnelResponse structureTunnelResponse = structureTunnel(0,
// WsDirection.DOWN);
// System.out.println(structureTunnelResponse);
// System.out.println();

// waitForMyTurn();
//
// for (WsBuilderunit wsBuilderunit : landingZone.getUnits()) {
// System.out.println(wsBuilderunit);
// }
//
// System.out.println(watch(0));

// List<WsCoordinate> wsCoordinates = new ArrayList<>();
// /*
// * for (int x = 2; x <= 5; x++) { for (int y = 16; y <= 19; y++) {
// wsCoordinates.add(new WsCoordinate(x, y));
// }
// * }
// */
// wsCoordinates.add(new WsCoordinate(3, 16));
// System.out.println(radar(0, wsCoordinates));
//
//

// waitForMyTurn();
//
// MoveBuilderUnitResponse moveBuilderUnitResponse = moveBuilderUnit(1,
// WsDirection.UP);
// System.out.println(moveBuilderUnitResponse);
// System.out.println();

// waitForMyTurn();
//
// MoveBuilderUnitRequest moveBuilderUnitRequest1 = new
// MoveBuilderUnitRequest();
// moveBuilderUnitRequest1.setUnit(1);
// moveBuilderUnitRequest1.setDirection(exitDirection);
// MoveBuilderUnitResponse moveBuilderUnitResponse1 =
// centralControl.moveBuilderUnit(moveBuilderUnitRequest1);
// System.out.println(moveBuilderUnitResponse1);
// System.out.println();

// waitForMyTurn();
//
// MoveBuilderUnitRequest moveBuilderUnitRequest2 = new
// MoveBuilderUnitRequest();
// moveBuilderUnitRequest2.setUnit(2);
// moveBuilderUnitRequest2.setDirection(exitDirection);
// MoveBuilderUnitResponse moveBuilderUnitResponse2 =
// centralControl.moveBuilderUnit(moveBuilderUnitRequest2);
// System.out.println(moveBuilderUnitResponse2);
// System.out.println();

//
// System.out.println();
// System.out.println(radarResponse.getResult());