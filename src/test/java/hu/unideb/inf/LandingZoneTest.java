package hu.unideb.inf;

import static hu.unideb.inf.LandingZonePart.*;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import eu.loxon.centralcontrol.CommonResp;
import eu.loxon.centralcontrol.GetSpaceShuttleExitPosResponse;
import eu.loxon.centralcontrol.GetSpaceShuttlePosResponse;
import eu.loxon.centralcontrol.ResultType;
import eu.loxon.centralcontrol.StartGameResponse;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsScore;

public class LandingZoneTest {

	private static LandingZone landingZone;

	@BeforeClass
	public static void initLandingZone() {
		CommonResp commonResp = new CommonResp();
		commonResp.setActionPointsLeft(20);
		commonResp.setBuilderUnit(0);
		commonResp.setCode("Test this.");
		commonResp.setExplosivesLeft(30);
		commonResp.setMessage("Haha");

		WsScore score = new WsScore();
		score.setBonus(200);
		score.setPenalty(-20);
		score.setReward(1000);
		score.setTotal(1200);
		commonResp.setScore(score);

		commonResp.setTurnsLeft(50);
		commonResp.setType(ResultType.DONE);

		StartGameResponse startGameResponse = new StartGameResponse();
		startGameResponse.setResult(commonResp);
		startGameResponse.setSize(new WsCoordinate(19, 19));

		GetSpaceShuttlePosResponse getSpaceShuttlePosResponse = new GetSpaceShuttlePosResponse();
		getSpaceShuttlePosResponse.setResult(commonResp);
		getSpaceShuttlePosResponse.setCord(new WsCoordinate(10, 10));

		GetSpaceShuttleExitPosResponse getSpaceShuttleExitPosResponse = new GetSpaceShuttleExitPosResponse();
		getSpaceShuttleExitPosResponse.setResult(commonResp);
		getSpaceShuttleExitPosResponse.setCord(new WsCoordinate(11, 10));

		landingZone = new LandingZone(startGameResponse, getSpaceShuttlePosResponse, getSpaceShuttleExitPosResponse);
	}

	@Test
	public void testDetermineLandingZonePart() {
		WsCoordinate[] bottomLeftCoordinates = new WsCoordinate[] { new WsCoordinate(0, 0), new WsCoordinate(5, 5) };
		WsCoordinate[] topLeftCoordinates = new WsCoordinate[] { new WsCoordinate(0, 19), new WsCoordinate(9, 19),
				new WsCoordinate(5, 17) };
		WsCoordinate[] bottomRightCoordinates = new WsCoordinate[] { new WsCoordinate(19, 0), new WsCoordinate(15, 8),
				new WsCoordinate(16, 5) };
		WsCoordinate[] topRightCoordinates = new WsCoordinate[] { new WsCoordinate(19, 19), new WsCoordinate(11, 17),
				new WsCoordinate(16, 16) };
		// WsCoordinate[] centerCoordinates = new WsCoordinate[] { new WsCoordinate(9, 9), new WsCoordinate(12, 12),
		// new WsCoordinate(7, 7), new WsCoordinate(12, 12) };

		for (int i = 0; i < bottomLeftCoordinates.length; i++) {
			assertEquals(BOTTOM_LEFT, landingZone.determineLandingZonePart(bottomLeftCoordinates[i]));
		}

		for (int i = 0; i < topLeftCoordinates.length; i++) {
			assertEquals(TOP_LEFT, landingZone.determineLandingZonePart(topLeftCoordinates[i]));
		}

		for (int i = 0; i < bottomRightCoordinates.length; i++) {
			assertEquals(BOTTOM_RIGHT, landingZone.determineLandingZonePart(bottomRightCoordinates[i]));
		}

		for (int i = 0; i < topRightCoordinates.length; i++) {
			assertEquals(TOP_RIGHT, landingZone.determineLandingZonePart(topRightCoordinates[i]));
		}

		// for (int i = 0; i < centerCoordinates.length; i++) {
		// assertEquals(CENTER, landingZone.determineLandingZonePart(centerCoordinates[i]));
		// }
	}

}
