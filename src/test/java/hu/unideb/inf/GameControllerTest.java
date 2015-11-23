package hu.unideb.inf;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

	@Test
	public void testCalculateCoordinate() {
		WsCoordinate upCoordinate = GameController.calculateWsCoordinate(wsCoordinate, WsDirection.UP);
		assertEquals(new WsCoordinate(10, 11), upCoordinate);

		WsCoordinate downCoordinate = GameController.calculateWsCoordinate(wsCoordinate, WsDirection.DOWN);
		assertEquals(new WsCoordinate(10, 9), downCoordinate);

		WsCoordinate leftCoordinate = GameController.calculateWsCoordinate(wsCoordinate, WsDirection.LEFT);
		assertEquals(new WsCoordinate(9, 10), leftCoordinate);

		WsCoordinate rightCoordinate = GameController.calculateWsCoordinate(wsCoordinate, WsDirection.RIGHT);
		assertEquals(new WsCoordinate(11, 10), rightCoordinate);
	}

	@Test
	public void testGetMinIndices1() {
		int[] points = new int[] { 1, 1, 2, 3, 3, 3 };

		List<Integer> minIndices = GameController.getMinIndices(points);
		Collections.sort(minIndices);

		List<Integer> expectedIndices = new ArrayList<>();
		expectedIndices.add(0);
		expectedIndices.add(1);
		Collections.sort(expectedIndices);

		assertEquals(expectedIndices, minIndices);
	}

	@Test
	public void testGetMinIndices2() {
		int[] points = new int[] { 2, 1, 1, 2, 3, 3, 3, 0 };

		List<Integer> minIndices = GameController.getMinIndices(points);
		Collections.sort(minIndices);

		List<Integer> expectedIndices = new ArrayList<>();
		expectedIndices.add(points.length - 1);
		Collections.sort(expectedIndices);

		assertEquals(expectedIndices, minIndices);
	}

	@Test
	public void testGetMinIndices3() {
		int[] points = new int[] { 2, 1, 1, 2, 3, 3, 3 };

		List<Integer> minIndices = GameController.getMinIndices(points);
		Collections.sort(minIndices);

		List<Integer> expectedIndices = new ArrayList<>();
		expectedIndices.add(1);
		expectedIndices.add(2);
		Collections.sort(expectedIndices);

		assertEquals(expectedIndices, minIndices);
	}

	@Test
	public void testGetMinIndices4() {
		int[] points = new int[] { 2, 2, 2, 2, 1, 1, 2, 3, 3, 3, 1, 3 };

		List<Integer> minIndices = GameController.getMinIndices(points);
		Collections.sort(minIndices);

		List<Integer> expectedIndices = new ArrayList<>();
		expectedIndices.add(4);
		expectedIndices.add(5);
		expectedIndices.add(points.length - 2);
		Collections.sort(expectedIndices);

		assertEquals(expectedIndices, minIndices);
	}

}
