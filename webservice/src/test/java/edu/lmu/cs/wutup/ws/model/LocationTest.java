package edu.lmu.cs.wutup.ws.model;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

public class LocationTest {

    @Test
    public void fieldsSetByConstructorCanBeRead() {
        Location loc = new Location("Gym", 33.969962, -118.4185002, 1.11);
        assertThat(loc.getAddress(), is("Gym"));
        assertThat(loc.getLatitude(), is(33.969962));
        assertThat(loc.getLongtitude(), is(-118.4185002));
        assertThat(loc.getAccuracy(), is(1.11));
    }

    @Test
    public void fieldsSetBySettersCanBeRead() {
        Location loc = new Location();
        loc.setAddress("pool");
        loc.setLatitude(30.000001);
        loc.setLongtitude(-118.000001);
        loc.setAccuracy(0.11);
        assertThat(loc.getAddress(), is("pool"));
        assertThat(loc.getLatitude(), is(30.000001));
        assertThat(loc.getLongtitude(), is(-118.000001));
        assertThat(loc.getAccuracy(), is(0.11));
    }
}