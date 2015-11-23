package hu.unideb.inf;

import org.junit.Assert;
import org.junit.Test;

import eu.loxon.centralcontrol.WsCoordinate;

public class WsCoordinateTest {

	@Test
	public void testDistanceFrom() {
		WsCoordinate coordinate = new WsCoordinate(10, 10);
		WsCoordinate firstNeighbor = new WsCoordinate(11, 10);
		WsCoordinate secondNeighbor = new WsCoordinate(12, 10);
		WsCoordinate thirdNeighbor = new WsCoordinate(13, 10);

		Assert.assertEquals(1, coordinate.distanceFrom(firstNeighbor));
		Assert.assertEquals(2, coordinate.distanceFrom(secondNeighbor));
		Assert.assertEquals(3, coordinate.distanceFrom(thirdNeighbor));
	}

}
