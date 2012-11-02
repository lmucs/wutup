package edu.lmu.cs.wutup.ws.dao.util;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class HaversineTest {
    
    @Test
    public void testGetDistanceInKilometers() {
        Double distance = Haversine.getDistanceInKilometers(38.898556, 38.897147, -170.037852, -77.043934);
        assertThat(Math.floor(distance), is(Math.floor(7645.497)));
    }
    
    @Test
    public void testGetDistanceInMiles() {
        Double distance = Haversine.getDistanceInMiles(38.898556, 38.897147, -170.037852, -77.043934);
        assertThat(Math.floor(distance), is(Math.floor(4751.893)));
    }
}
