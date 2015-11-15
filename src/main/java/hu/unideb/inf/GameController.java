package hu.unideb.inf;

import java.util.ArrayList;
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
import eu.loxon.centralcontrol.ResultType;
import eu.loxon.centralcontrol.Scouting;
import eu.loxon.centralcontrol.StartGameResponse;
import eu.loxon.centralcontrol.StructureTunnelRequest;
import eu.loxon.centralcontrol.StructureTunnelResponse;
import eu.loxon.centralcontrol.WatchRequest;
import eu.loxon.centralcontrol.WatchResponse;
import eu.loxon.centralcontrol.WsBuilderunit;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;

public class GameController {

	private static ObjectFactory objectFactory = new ObjectFactory();
	private CentralControl centralControl;
	private LandingZone landingZone;
	private long lastIsMyTurnRequest = 0L;

	public GameController(CentralControl centralControl) {
		this.centralControl = centralControl;
	}

	public void playGame() {
		StartGameResponse startGameResponse = startGame();
		GetSpaceShuttlePosResponse getSpaceShuttlePosResponse = getSpaceShuttlePos();
		GetSpaceShuttleExitPosResponse getSpaceShuttleExitPosResponse = getSpaceShuttleExitPos();

		this.landingZone = new LandingZone(startGameResponse, getSpaceShuttlePosResponse,
				getSpaceShuttleExitPosResponse);

		System.out.println(landingZone);
		System.out.println();
		System.out.println(getSpaceShuttleExitPosResponse.getResult());
		System.out.println();

		for (int i = 0; i < 20; i++) {
			

			
			//waitForMyTurn();
			System.out.println(watch(waitForMyTurn()));
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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
	}

	private WatchResponse watch(int unit) {
		WatchRequest watchRequest = objectFactory.createWatchRequest();

		watchRequest.setUnit(unit);

		return centralControl.watch(watchRequest);
	}

	private RadarResponse radar(int unit, List<WsCoordinate> wsCoordinates) {
		RadarRequest radarRequest = objectFactory.createRadarRequest();

		radarRequest.setUnit(unit);
		radarRequest.getCord().addAll(wsCoordinates);

		return centralControl.radar(radarRequest);
	}

	private MoveBuilderUnitResponse moveBuilderUnit(int unit, WsDirection wsDirection) {
		MoveBuilderUnitRequest moveBuilderUnitRequest = new MoveBuilderUnitRequest();

		moveBuilderUnitRequest.setUnit(unit);
		moveBuilderUnitRequest.setDirection(wsDirection);

		MoveBuilderUnitResponse moveBuilderUnitResponse = centralControl.moveBuilderUnit(moveBuilderUnitRequest);
		if (moveBuilderUnitResponse.getResult().getType().equals(ResultType.DONE)) {
			landingZone.set(unit, calculateWsCoordinate(landingZone.getUnitPosition()[unit], wsDirection));
		}

		return moveBuilderUnitResponse;
	}

	private WsCoordinate calculateWsCoordinate(WsCoordinate start, WsDirection wsDirection) {
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

	private int waitForMyTurn() {
//		do {
//			try {
//				Thread.sleep(50);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		} while ((System.currentTimeMillis() - this.lastIsMyTurnRequest) < 350);

		boolean isMyTurn = false;
		do {
			IsMyTurnRequest isMyTurnRequest = new IsMyTurnRequest();
			IsMyTurnResponse isMyTurnResponse = centralControl.isMyTurn(isMyTurnRequest);
			System.out.println(isMyTurnResponse);

			isMyTurn = isMyTurnResponse.isIsYourTurn();
			if (isMyTurn) {
				this.lastIsMyTurnRequest = System.currentTimeMillis();
				// try {
				// Thread.sleep(350);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				return isMyTurnResponse.getResult().getBuilderUnit();
			} else {
				try {
					Thread.sleep(350);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while (!isMyTurn);
		
		return -1;
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
