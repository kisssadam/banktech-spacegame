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

	private static final ObjectFactory objectFactory = new ObjectFactory();

	private CentralControl centralControl;
	private LandingZone landingZone;
	private long lastIsMyTurnRequest;
	private int actualBuilderUnit;
	private WsDirection[] lastDirections;

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
	}

	public void playGame() throws InterruptedException {
		doFirstSteps();

		// IsMyTurnResponse isMyTurnResponse = waitForMyTurn();
		// while (isMyTurnResponse.getResult().getTurnsLeft() > 0) {
		// doNextStep(isMyTurnResponse);
		// }
	}

	private WsDirection determineBestDirection(WsCoordinate unitPos) {
		if (unitPos.equals(landingZone.getSpaceShuttlePos())) {
			return calculateDirection(landingZone.getSpaceShuttlePos(), landingZone.getSpaceShuttleExitPos());
		}

		WatchResponse watchResponse = watch(this.actualBuilderUnit);
		List<Scouting> scoutings = watchResponse.getScout();
		ActionCostResponse costResponse = getActionCost();
		int min = Integer.MAX_VALUE, minIndex = 0;
		for (int i = 0; i < scoutings.size(); i++) {
			int rating = rateCell(scoutings.get(i), costResponse);
			if (rating < min) {
				min = rating;
				minIndex = i;
			}
		}

		return calculateDirection(unitPos, scoutings.get(minIndex).getCord());
	}

	private int rateCell(Scouting scouting, ActionCostResponse costResponse) {
		int points = 0;

		switch (scouting.getObject()) {
		case TUNNEL:
			if (TEAM_NAME.equals(scouting.getTeam())) {
				points = costResponse.getMove();
			} else {
				points = costResponse.getExplode();
			}
			break;

		case SHUTTLE:
			points = Integer.MAX_VALUE;
			break;

		case BUILDER_UNIT:
			points = Integer.MAX_VALUE;
			break;

		case ROCK:
			points = costResponse.getDrill();
			break;

		case GRANITE:
			points = costResponse.getExplode();
			break;

		case OBSIDIAN:
			points = Integer.MAX_VALUE;
			break;

		case UNINITIALIZED:
			break;
		}

		return points;
	}

	// private LandingZonePart determineLandingZonePart(WsCoordinate coordinate) {
	// WsCoordinate central = landingZone.determineCentralCoordinates();
	// int centralRadius = landingZone.determineCentralRadius();
	//
	// int x = coordinate.getX();
	// int y = coordinate.getY();
	//
	// if (x > central.getX() - centralRadius && x < central.getX() + centralRadius
	// && y > central.getY() - centralRadius && y < central.getY() + centralRadius) {
	// return LandingZonePart.CENTER;
	// } else if (x <= central.getX() && y >= central.getY()) {
	// return LandingZonePart.TOP_LEFT;
	// } else if (x > central.getX() && y >= central.getY()) {
	// return LandingZonePart.TOP_RIGHT;
	// } else if (x <= central.getX() && y < central.getY()) {
	// return LandingZonePart.BOTTOM_LEFT;
	// } else {
	// return LandingZonePart.BOTTOM_RIGHT;
	// }
	// }

	private void doFirstSteps() {
		WsCoordinate spaceShuttlePos = landingZone.getSpaceShuttlePos();
		WsCoordinate spaceShuttleExitPos = landingZone.getSpaceShuttleExitPos();
		WsDirection exitDirection = calculateDirection(spaceShuttlePos, spaceShuttleExitPos);

		System.out.println(landingZone);

		waitForMyTurn();
		watch(actualBuilderUnit);
		structureTunnel(actualBuilderUnit, exitDirection);

		System.out.println(landingZone);

		List<WsCoordinate> coordinatesToRadar = determineUnitZeroRadarCells(
				landingZone.getUnitPosition(actualBuilderUnit), exitDirection);
		radar(actualBuilderUnit, coordinatesToRadar);

		System.out.println(landingZone);

		waitForMyTurn();
		while (actualBuilderUnit == 0) {
			waitForMyTurn();
		}

		moveBuilderUnit(actualBuilderUnit, exitDirection);

		System.out.println(landingZone);

		List<Scouting> scoutings = watch(actualBuilderUnit).getScout();
		WsCoordinate bestCoordinate = getBestCoordinate(scoutings);
		doNextStep(bestCoordinate);

		System.out.println(landingZone);

		//

		// WatchResponse watchResponse = watch(this.actualBuilderUnit);
		// List<WsCoordinate> coordinatesToRemove = new ArrayList<WsCoordinate>(8);
		// for (Scouting scouting : watchResponse.getScout()) {
		// coordinatesToRemove.add(scouting.getCord());
		// }
		//
		// System.out.println(landingZone);
		// structureTunnel(this.actualBuilderUnit, exitDirection);
		//
		// moveBuilderUnit(this.actualBuilderUnit, exitDirection);
		//
		// watchResponse = watch(this.actualBuilderUnit);
		// for (Scouting scouting : watchResponse.getScout()) {
		// coordinatesToRemove.add(scouting.getCord());
		// }
		//
		// WsCoordinate shuttlePos = landingZone.getSpaceShuttlePos();
		// List<WsCoordinate> coordinatesToScan = createRadarZone(shuttlePos);
		//
		// coordinatesToScan.removeAll(coordinatesToRemove);
		//
		// while (coordinatesToScan.size() != 0) {
		// IsMyTurnResponse waitForMyTurn = waitForMyTurn();
		// if (this.actualBuilderUnit == 0) {
		// List<WsCoordinate> unitZeroRadarCells = determineUnitZeroRadarCells(
		// landingZone.getUnitPosition(this.actualBuilderUnit), exitDirection);
		// radar(this.actualBuilderUnit, unitZeroRadarCells);
		// } else {
		// int radarableCells = waitForMyTurn.getResult().getActionPointsLeft() / getActionCost().getRadar();
		// List<WsCoordinate> subList = coordinatesToScan.subList(0,
		// radarableCells > coordinatesToScan.size() ? coordinatesToScan.size() : radarableCells);
		// radar(this.actualBuilderUnit, subList);
		// coordinatesToScan.removeAll(subList);
		//
		// // watch(actualBuilderUnit);
		// }
		// }
		//
		// System.out.println(landingZone);
	}

	private void doNextStep(WsCoordinate coordinate) {
		ObjectType objectType = landingZone.getTerrainOfCell(coordinate);

		WsCoordinate actualUnitPosition = landingZone.getUnitPosition(actualBuilderUnit);
		WsDirection direction = calculateDirection(actualUnitPosition, coordinate);

		switch (objectType) {
		case TUNNEL:
			if (TEAM_NAME.equals(landingZone.getTeamOfCell(coordinate))) {
				moveBuilderUnit(actualBuilderUnit, direction);
			} else {
				explodeCell(actualBuilderUnit, direction);
			}
			break;

		case GRANITE:
			explodeCell(actualBuilderUnit, direction);
			break;

		case ROCK:
			structureTunnel(actualBuilderUnit, direction);
			break;

		default:
			System.out.println("Should not happen!");
			break;
		}
	}

	// TODO mi van ha mindegyik Integer.MAX_VALUE ? Ekkor is visszakuld egy koordinatat. Ezt kezelni kell.
	private WsCoordinate getBestCoordinate(List<Scouting> scoutings) {
		int[] points = new int[4];

		for (int i = 0; i < scoutings.size(); i++) {
			Scouting scouting = scoutings.get(i);

			switch (scouting.getObject()) {
			case TUNNEL:
				if (TEAM_NAME.equals(scouting.getTeam())) {
					WsDirection direction = calculateDirection(landingZone.getUnitPosition(actualBuilderUnit),
							scouting.getCord());
					if (lastDirections[actualBuilderUnit] != direction) {
						points[i] = 1;
					} else {
						points[i] = 5;
					}
				} else {
					points[i] = 4;
				}
				break;

			case GRANITE:
				points[i] = 3;
				break;

			case ROCK:
				points[i] = 2;
				break;

			default:
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

		return scoutings.get(minIndex).getCord();
	}

	private List<WsCoordinate> determineUnitZeroRadarCells(WsCoordinate builderUnitPosition,
			WsDirection exitDirection) {
		List<WsCoordinate> coordinates = new ArrayList<>(4);

		int x = builderUnitPosition.getX();
		int y = builderUnitPosition.getY();

		switch (exitDirection) {
		case UP:
			coordinates.add(new WsCoordinate(x - 1, y + 1));
			coordinates.add(new WsCoordinate(x - 2, y + 1));

			coordinates.add(new WsCoordinate(x + 1, y + 1));
			coordinates.add(new WsCoordinate(x + 2, y + 1));

			coordinates.add(new WsCoordinate(x, y + 2));
			coordinates.add(new WsCoordinate(x, y + 3));
			break;

		case DOWN:
			coordinates.add(new WsCoordinate(x - 1, y - 1));
			coordinates.add(new WsCoordinate(x - 2, y - 1));

			coordinates.add(new WsCoordinate(x + 1, y - 1));
			coordinates.add(new WsCoordinate(x + 2, y - 1));

			coordinates.add(new WsCoordinate(x, y - 2));
			coordinates.add(new WsCoordinate(x, y - 3));
			break;

		case LEFT:
			coordinates.add(new WsCoordinate(x - 1, y + 1));
			coordinates.add(new WsCoordinate(x - 1, y + 2));

			coordinates.add(new WsCoordinate(x - 1, y - 1));
			coordinates.add(new WsCoordinate(x - 1, y - 2));

			coordinates.add(new WsCoordinate(x - 2, y));
			coordinates.add(new WsCoordinate(x - 3, y));
			break;

		case RIGHT:
			coordinates.add(new WsCoordinate(x + 1, y + 1));
			coordinates.add(new WsCoordinate(x + 1, y + 2));

			coordinates.add(new WsCoordinate(x + 1, y - 1));
			coordinates.add(new WsCoordinate(x + 1, y - 2));

			coordinates.add(new WsCoordinate(x + 2, y));
			coordinates.add(new WsCoordinate(x + 3, y));
			break;
		}

		removeInvalidCoordinates(coordinates);

		return coordinates;
	}

	private void removeInvalidCoordinates(List<WsCoordinate> coordinates) {
		for (Iterator<WsCoordinate> iterator = coordinates.iterator(); iterator.hasNext();) {
			WsCoordinate wsCoordinate = (WsCoordinate) iterator.next();

			if (!isCoordinateValid(wsCoordinate)) {
				iterator.remove();
			}
		}
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

	private WatchResponse watch(int unit) {
		WatchRequest watchRequest = objectFactory.createWatchRequest();
		watchRequest.setUnit(unit);

		WatchResponse response = centralControl.watch(watchRequest);
		System.out.println(response);

		updateActualBuilderUnit(response.getResult());
		landingZone.processScoutings(response.getScout());

		return response;
	}

	private RadarResponse radar(int unit, List<WsCoordinate> wsCoordinates) {
		RadarRequest radarRequest = objectFactory.createRadarRequest();

		radarRequest.setUnit(unit);
		radarRequest.getCord().addAll(wsCoordinates);

		RadarResponse response = centralControl.radar(radarRequest);
		System.out.println(response);

		updateActualBuilderUnit(response.getResult());
		landingZone.processScoutings(response.getScout());

		return response;
	}

	private MoveBuilderUnitResponse moveBuilderUnit(int unit, WsDirection wsDirection) {
		MoveBuilderUnitRequest moveBuilderUnitRequest = objectFactory.createMoveBuilderUnitRequest();

		moveBuilderUnitRequest.setUnit(unit);
		moveBuilderUnitRequest.setDirection(wsDirection);

		MoveBuilderUnitResponse response = centralControl.moveBuilderUnit(moveBuilderUnitRequest);
		System.out.println(response);

		if (response.getResult().getType().equals(ResultType.DONE)) {
			landingZone.setUnitPosition(unit, calculateWsCoordinate(landingZone.getUnitPosition(unit), wsDirection));
			lastDirections[actualBuilderUnit] = wsDirection;
		}

		updateActualBuilderUnit(response.getResult());
		landingZone.setTerrain(landingZone.getUnitPosition(unit), ObjectType.BUILDER_UNIT);

		return response;
	}

	private WsCoordinate calculateWsCoordinate(WsCoordinate start, WsDirection wsDirection) {
		WsCoordinate wsCoordinate = null;

		switch (wsDirection) {
		case UP:
			wsCoordinate = new WsCoordinate(start.getX(), start.getY() + 1);
		case DOWN:
			wsCoordinate = new WsCoordinate(start.getX(), start.getY() - 1);
		case LEFT:
			wsCoordinate = new WsCoordinate(start.getX() - 1, start.getY());
		case RIGHT:
			wsCoordinate = new WsCoordinate(start.getX() + 1, start.getY());
		}

		return wsCoordinate;
	}

	private void updateActualBuilderUnit(CommonResp result) {
		this.actualBuilderUnit = result.getBuilderUnit();
		System.out.println("Actual builder unit: " + this.actualBuilderUnit);
	}

	private StartGameResponse startGame() {
		StartGameResponse response = centralControl.startGame(objectFactory.createStartGameRequest());
		System.out.println(response);

		updateActualBuilderUnit(response.getResult());

		return response;
	}

	private GetSpaceShuttlePosResponse getSpaceShuttlePos() {
		GetSpaceShuttlePosRequest request = objectFactory.createGetSpaceShuttlePosRequest();

		GetSpaceShuttlePosResponse response = centralControl.getSpaceShuttlePos(request);
		System.out.println(response);

		updateActualBuilderUnit(response.getResult());
		return response;
	}

	private GetSpaceShuttleExitPosResponse getSpaceShuttleExitPos() {
		GetSpaceShuttleExitPosRequest request = objectFactory.createGetSpaceShuttleExitPosRequest();

		GetSpaceShuttleExitPosResponse response = centralControl.getSpaceShuttleExitPos(request);
		System.out.println(response);

		updateActualBuilderUnit(response.getResult());

		return response;
	}

	private ExplodeCellResponse explodeCell(int unit, WsDirection wsDirection) {
		ExplodeCellRequest explodeCellRequest = objectFactory.createExplodeCellRequest();

		explodeCellRequest.setUnit(unit);
		explodeCellRequest.setDirection(wsDirection);

		ExplodeCellResponse response = centralControl.explodeCell(explodeCellRequest);
		System.out.println(response);

		updateActualBuilderUnit(response.getResult());

		return response;
	}

	private ActionCostResponse getActionCost() {
		ActionCostResponse response = centralControl.getActionCost(objectFactory.createActionCostRequest());
		System.out.println(response);

		updateActualBuilderUnit(response.getResult());

		return response;
	}

	private StructureTunnelResponse structureTunnel(int unit, WsDirection wsDirection) {
		StructureTunnelRequest structureTunnelRequest = new StructureTunnelRequest();

		structureTunnelRequest.setUnit(unit);
		structureTunnelRequest.setDirection(wsDirection);

		StructureTunnelResponse response = centralControl.structureTunnel(structureTunnelRequest);
		System.out.println(response);

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
		long waitTime = 300L;
		long timeDifference = System.currentTimeMillis() - this.lastIsMyTurnRequest;
		if (timeDifference < waitTime) {
			Thread.sleep(waitTime - timeDifference);
		}

		boolean isMyTurn = false;
		do {
			IsMyTurnRequest isMyTurnRequest = new IsMyTurnRequest();
			response = centralControl.isMyTurn(isMyTurnRequest);
			this.lastIsMyTurnRequest = System.currentTimeMillis();
			System.out.println(response);

			updateActualBuilderUnit(response.getResult());

			isMyTurn = response.isIsYourTurn();
			if (isMyTurn) {
				break;
			} else {
				Thread.sleep(waitTime);
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
