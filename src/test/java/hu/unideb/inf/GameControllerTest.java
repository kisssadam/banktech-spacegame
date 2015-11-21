package hu.unideb.inf;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;

public class GameControllerTest {

	private static WsCoordinate wsCoordinate;

	@BeforeClass
	public static void initWsCoordinate() {
		wsCoordinate = new WsCoordinate(10, 10);
	}

	@Test
	public void testCalculateDirectionLeft() {
		WsCoordinate leftCoordinate = new WsCoordinate(9, 10);
		WsDirection leftDirection = GameController.calculateDirection(wsCoordinate, leftCoordinate);

		assertEquals(WsDirection.LEFT, leftDirection);
	}

	@Test
	public void testCalculateDirectionRight() {
		WsCoordinate rightCoordinate = new WsCoordinate(11, 10);
		WsDirection rightDirection = GameController.calculateDirection(wsCoordinate, rightCoordinate);

		assertEquals(WsDirection.RIGHT, rightDirection);
	}

	@Test
	public void testCalculateDirectionDown() {
		WsCoordinate downCoordinate = new WsCoordinate(10, 9);
		WsDirection DownDirection = GameController.calculateDirection(wsCoordinate, downCoordinate);

		assertEquals(WsDirection.DOWN, DownDirection);
	}

	@Test
	public void testCalculateDirectionUp() {
		WsCoordinate upCoordinate = new WsCoordinate(10, 11);
		WsDirection UpDirection = GameController.calculateDirection(wsCoordinate, upCoordinate);

		assertEquals(WsDirection.UP, UpDirection);
	}

}
