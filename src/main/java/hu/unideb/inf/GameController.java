package hu.unideb.inf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.loxon.centralcontrol.ActionCostResponse;
import eu.loxon.centralcontrol.CentralControl;
import eu.loxon.centralcontrol.CommonResp;
import eu.loxon.centralcontrol.ExplodeCellRequest;
import eu.loxon.centralcontrol.ExplodeCellResponse;
import eu.loxon.centralcontrol.GetSpaceShuttleExitPosRequest;
import eu.loxon.centralcontrol.GetSpaceShuttleExitPosResponse;
import eu.loxon.centralcontrol.GetSpaceShuttlePosRequest;
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
import eu.loxon.centralcontrol.StartGameResponse;
import eu.loxon.centralcontrol.StructureTunnelRequest;
import eu.loxon.centralcontrol.StructureTunnelResponse;
import eu.loxon.centralcontrol.WatchRequest;
import eu.loxon.centralcontrol.WatchResponse;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;

public class GameController {

	public static final String TEAM_NAME = "0x70unideb";

	private static final ObjectFactory objectFactory = new ObjectFactory();

	private CentralControl centralControl;
	private LandingZone landingZone;
	private long lastIsMyTurnRequest;
	private int actualBuilderUnit;
	private WsDirection[] lastDirections;
	private CommonResp lastCommonResp;
	private WsDirection exitDirection;
	ActionCostResponse actionCostResponse;

	public GameController() {
		super();
	}

	public GameController(CentralControl centralControl) {
		this.centralControl = centralControl;

		StartGameResponse startGameResponse = startGame();
		GetSpaceShuttlePosResponse shuttlePosResponse = getSpaceShuttlePos();
		GetSpaceShuttleExitPosResponse shuttleExitPosResponse = getSpaceShuttleExitPos();

		this.landingZone = new LandingZone(startGameResponse, shuttlePosResponse, shuttleExitPosResponse);
		this.lastDirections = new WsDirection[4];

		WsCoordinate spaceShuttlePos = landingZone.getSpaceShuttlePos();
		WsCoordinate spaceShuttleExitPos = landingZone.getSpaceShuttleExitPos();
		this.exitDirection = calculateDirection(spaceShuttlePos, spaceShuttleExitPos);

		this.actionCostResponse = getActionCost();
	}

	public void playGame() throws InterruptedException {
		System.out.println(landingZone);

		do {
			waitForMyTurn();
			doNextAction();
			System.out.println(landingZone);
		} while (lastCommonResp.getTurnsLeft() > 0);
	}

	private void doNextAction() {
		WsCoordinate actualCoordinate = landingZone.getUnitPosition(actualBuilderUnit);

		watch(actualBuilderUnit);
		if (actualCoordinate.equals(landingZone.getSpaceShuttlePos())) {
			if (actualBuilderUnit == 0) {
				if (lastCommonResp.getActionPointsLeft() >= actionCostResponse.getDrill()) {
					structureTunnel(actualBuilderUnit, exitDirection);
				}
			} else {
				if (!landingZone.isThereAnyBuilderUnitOnCoordinate(landingZone.getSpaceShuttleExitPos())) {
					if (lastCommonResp.getActionPointsLeft() >= actionCostResponse.getMove()) {
						moveBuilderUnit(actualBuilderUnit, exitDirection);
					}
				} else {
					System.out.println("BUILDER_UNIT IS ON " + landingZone.getSpaceShuttleExitPos()
							+ " SO CAN'T STEP TO THE CELL.");
				}
			}
		} else {
			WsCoordinate bestCoordinate = getBestCoordinate();
			doNextStep(bestCoordinate);
		}
	}

	private void doNextStep(WsCoordinate coordinate) {
		ActionCostResponse actionCostResponse = getActionCost();

		ObjectType objectType = landingZone.getTerrainOfCell(coordinate);

		WsCoordinate actualUnitPosition = landingZone.getUnitPosition(actualBuilderUnit);
		WsDirection direction = calculateDirection(actualUnitPosition, coordinate);

		switch (objectType) {
		case TUNNEL:
			System.out.println("CSAPAT: *" + TEAM_NAME + "* *" + landingZone.getTeamOfCell(coordinate) + "*");
			if (TEAM_NAME.equals(landingZone.getTeamOfCell(coordinate))) {
				if (lastCommonResp.getActionPointsLeft() >= actionCostResponse.getMove()) {
					moveBuilderUnit(actualBuilderUnit, direction);
				}
			} else {
				if (lastCommonResp.getActionPointsLeft() >= actionCostResponse.getExplode()) {
					explodeCell(actualBuilderUnit, direction);
				}
			}
			break;

		case GRANITE:
			if (lastCommonResp.getActionPointsLeft() >= actionCostResponse.getExplode()) {
				explodeCell(actualBuilderUnit, direction);
			}
			break;

		case ROCK:
			if (lastCommonResp.getActionPointsLeft() >= actionCostResponse.getDrill()) {
				structureTunnel(actualBuilderUnit, direction);
			}
			break;

		default:
			System.out.println("I won't do anything, because the selected objectType is " + objectType);
			break;
		}
	}

	private WsCoordinate getBestCoordinate() {
		// TODO tuti ide kell? A jelenlegi kod eseten kell ez ide?
		if (landingZone.getUnitPosition(actualBuilderUnit).equals(landingZone.getSpaceShuttlePos())) {
			return landingZone.getSpaceShuttleExitPos();
		}

		int[] points = new int[4];

		WsCoordinate unitPosition = landingZone.getUnitPosition(actualBuilderUnit);
		WsCoordinate[] neightborCoordinates = unitPosition.getNeightborCoordinates();

		for (int i = 0; i < neightborCoordinates.length; i++) {
			WsCoordinate neighbor = neightborCoordinates[i];
			ObjectType objectType = landingZone.getTerrainOfCell(neighbor);

			switch (objectType) {
			case TUNNEL:
				System.out.println("TUNNEL lett kiválasztva.");
				if (TEAM_NAME.equals(landingZone.getTeamOfCell(neighbor))) {
					WsDirection direction = calculateDirection(landingZone.getUnitPosition(actualBuilderUnit),
							neighbor);
					if (lastDirections[actualBuilderUnit].opposite() != direction) {
						points[i] = 1;
					} else {
						points[i] = 5;
					}
				} else {
					points[i] = 4;
				}
				break;

			case GRANITE:
				System.out.println("GRANITE lett kiválasztva.");
				points[i] = 3;
				break;

			case ROCK:
				System.out.println("ROCK lett kiválasztva.");
				points[i] = 2;
				break;

			case BUILDER_UNIT:
				System.out.println("BUILDER_UNIT lett kiválasztva.");
				points[i] = Integer.MAX_VALUE;
				break;

			default:
				System.out.println("DEFAULT lett kiválasztva.");
				points[i] = Integer.MAX_VALUE;
				break;
			}
		}

		int minIndex = 0;
		for (int i = 0; i < points.length; i++) {
			if (points[i] < points[minIndex]) {
				minIndex = i;
			}
		}

		System.out.println("A kiválasztás végeredménye: " + neightborCoordinates[minIndex]);

		return neightborCoordinates[minIndex];
	}

	private boolean isCoordinateValid(WsCoordinate wsCoordinate) {
		WsCoordinate size = landingZone.getSize();

		boolean isXCoordinateValid = isCoordinateBetweenBounds(wsCoordinate.getX(), 0, size.getX());
		boolean isYCoordinateValid = isCoordinateBetweenBounds(wsCoordinate.getY(), 0, size.getY());

		return isXCoordinateValid && isYCoordinateValid;
	}

	private boolean isCoordinateBetweenBounds(int coordinate, int lowerInclusiveBound, int upperInclusiveBound) {
		return coordinate >= lowerInclusiveBound && coordinate <= upperInclusiveBound;
	}

	private List<WsCoordinate> createRadarZone(WsCoordinate center) {
		List<WsCoordinate> coordinatesToScan = new ArrayList<WsCoordinate>(49);

		for (int x = -3; x <= 3; x++) {
			for (int y = -3; y <= 3; y++) {
				coordinatesToScan.add(new WsCoordinate(x + center.getX(), y + center.getY()));
			}
		}
		removeInvalidCoordinates(coordinatesToScan);

		return coordinatesToScan;
	}

	private void removeInvalidCoordinates(List<WsCoordinate> coordinates) {
		for (Iterator<WsCoordinate> iterator = coordinates.iterator(); iterator.hasNext();) {
			WsCoordinate wsCoordinate = (WsCoordinate) iterator.next();

			if (!isCoordinateValid(wsCoordinate)) {
				iterator.remove();
			}
		}
	}

	private WatchResponse watch(int unit) {
		WatchRequest watchRequest = objectFactory.createWatchRequest();
		watchRequest.setUnit(unit);

		WatchResponse response = centralControl.watch(watchRequest);
		lastCommonResp = response.getResult();

		System.out.println();
		System.out.println(response);
		System.out.println();

		updateActualBuilderUnit(response.getResult());
		if (response.getResult().getType().equals(ResultType.DONE)) {
			if (!response.getScout().isEmpty()) {
				landingZone.processScoutings(response.getScout());
			}
		}

		return response;
	}

	private RadarResponse radar(int unit, List<WsCoordinate> wsCoordinates) {
		RadarRequest radarRequest = objectFactory.createRadarRequest();

		radarRequest.setUnit(unit);
		radarRequest.getCord().addAll(wsCoordinates);

		RadarResponse response = centralControl.radar(radarRequest);
		lastCommonResp = response.getResult();

		System.out.println();
		System.out.println(response);
		System.out.println();

		updateActualBuilderUnit(response.getResult());
		if (response.getResult().getType().equals(ResultType.DONE)) {
			if (!response.getScout().isEmpty()) {
				landingZone.processScoutings(response.getScout());
			}
		}

		return response;
	}

	private MoveBuilderUnitResponse moveBuilderUnit(int unit, WsDirection wsDirection) {
		MoveBuilderUnitRequest moveBuilderUnitRequest = objectFactory.createMoveBuilderUnitRequest();

		moveBuilderUnitRequest.setUnit(unit);
		moveBuilderUnitRequest.setDirection(wsDirection);

		MoveBuilderUnitResponse response = centralControl.moveBuilderUnit(moveBuilderUnitRequest);
		lastCommonResp = response.getResult();

		System.out.println();
		System.out.println(response);
		System.out.println();

		if (response.getResult().getType().equals(ResultType.DONE)) {
			landingZone.setUnitPosition(unit, calculateWsCoordinate(landingZone.getUnitPosition(unit), wsDirection));
			landingZone.setTerrain(landingZone.getUnitPosition(unit), ObjectType.BUILDER_UNIT);
			lastDirections[actualBuilderUnit] = wsDirection;
		}

		updateActualBuilderUnit(response.getResult());

		return response;
	}

	public static WsCoordinate calculateWsCoordinate(WsCoordinate start, WsDirection wsDirection) {
		WsCoordinate wsCoordinate = null;

		switch (wsDirection) {
		case UP:
			wsCoordinate = new WsCoordinate(start.getX(), start.getY() + 1);
			break;

		case DOWN:
			wsCoordinate = new WsCoordinate(start.getX(), start.getY() - 1);
			break;

		case LEFT:
			wsCoordinate = new WsCoordinate(start.getX() - 1, start.getY());
			break;

		case RIGHT:
			wsCoordinate = new WsCoordinate(start.getX() + 1, start.getY());
			break;
		}

		return wsCoordinate;
	}

	private void updateActualBuilderUnit(CommonResp result) {
		this.actualBuilderUnit = result.getBuilderUnit();
		System.out.println("Actual builder unit: " + this.actualBuilderUnit);
	}

	private StartGameResponse startGame() {
		StartGameResponse response = centralControl.startGame(objectFactory.createStartGameRequest());
		lastCommonResp = response.getResult();

		System.out.println();
		System.out.println(response);
		System.out.println();

		updateActualBuilderUnit(response.getResult());

		return response;
	}

	private GetSpaceShuttlePosResponse getSpaceShuttlePos() {
		GetSpaceShuttlePosRequest request = objectFactory.createGetSpaceShuttlePosRequest();

		GetSpaceShuttlePosResponse response = centralControl.getSpaceShuttlePos(request);
		lastCommonResp = response.getResult();

		System.out.println();
		System.out.println(response);
		System.out.println();

		updateActualBuilderUnit(response.getResult());
		return response;
	}

	private GetSpaceShuttleExitPosResponse getSpaceShuttleExitPos() {
		GetSpaceShuttleExitPosRequest request = objectFactory.createGetSpaceShuttleExitPosRequest();

		GetSpaceShuttleExitPosResponse response = centralControl.getSpaceShuttleExitPos(request);
		lastCommonResp = response.getResult();

		System.out.println();
		System.out.println(response);
		System.out.println();

		updateActualBuilderUnit(response.getResult());

		return response;
	}

	private ExplodeCellResponse explodeCell(int unit, WsDirection wsDirection) {
		ExplodeCellRequest explodeCellRequest = objectFactory.createExplodeCellRequest();

		explodeCellRequest.setUnit(unit);
		explodeCellRequest.setDirection(wsDirection);

		ExplodeCellResponse response = centralControl.explodeCell(explodeCellRequest);
		lastCommonResp = response.getResult();

		System.out.println(response);

		updateActualBuilderUnit(response.getResult());

		return response;
	}

	private ActionCostResponse getActionCost() {
		ActionCostResponse response = centralControl.getActionCost(objectFactory.createActionCostRequest());
		lastCommonResp = response.getResult();

		System.out.println();
		System.out.println(response);
		System.out.println();

		updateActualBuilderUnit(response.getResult());

		return response;
	}

	private StructureTunnelResponse structureTunnel(int unit, WsDirection wsDirection) {
		StructureTunnelRequest structureTunnelRequest = new StructureTunnelRequest();

		structureTunnelRequest.setUnit(unit);
		structureTunnelRequest.setDirection(wsDirection);

		System.out.println();
		System.out.println(structureTunnelRequest);

		StructureTunnelResponse response = centralControl.structureTunnel(structureTunnelRequest);
		lastCommonResp = response.getResult();

		System.out.println(response);
		System.out.println();

		if (ResultType.DONE.equals(response.getResult().getType())) {
			WsCoordinate wsCoordinate = calculateWsCoordinate(landingZone.getUnitPosition(unit), wsDirection);
			landingZone.setTerrain(wsCoordinate, ObjectType.TUNNEL);
		}

		updateActualBuilderUnit(response.getResult());

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

		final long requiredTimeToWait = 300L;
		long elapsedTimeSinceLastIsMyTurnRequest = System.currentTimeMillis() - this.lastIsMyTurnRequest;
		if (elapsedTimeSinceLastIsMyTurnRequest < requiredTimeToWait) {
			Thread.sleep(requiredTimeToWait - elapsedTimeSinceLastIsMyTurnRequest);
		}

		boolean isMyTurn = false;
		do {
			IsMyTurnRequest isMyTurnRequest = new IsMyTurnRequest();
			response = centralControl.isMyTurn(isMyTurnRequest);
			this.lastIsMyTurnRequest = System.currentTimeMillis();
			lastCommonResp = response.getResult();

			System.out.println();
			System.out.println(response);
			System.out.println();

			updateActualBuilderUnit(response.getResult());

			isMyTurn = response.isIsYourTurn();
			if (isMyTurn) {
				break;
			} else {
				Thread.sleep(requiredTimeToWait);
			}
		} while (!isMyTurn);

		return response;
	}

	public static WsDirection calculateDirection(WsCoordinate startPos, WsCoordinate destinationPos) {
		int diffX = destinationPos.getX() - startPos.getX();
		int diffY = destinationPos.getY() - startPos.getY();

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
