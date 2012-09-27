package edu.lmu.cs.wutup.ws.model;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertFalse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import org.junit.Test;

public class LocationTest {

    @Test
    public void fieldsSetByConstructorCanBeRead() {
        Location loc = new Location(123456, "Keck Lab", 33.969962, -118.4185002, "Doolan");
        assertThat(loc.getId(), is(123456));
        assertThat(loc.getAddress(), is("Keck Lab"));
        assertThat(loc.getLatitude(), is(33.969962));
        assertThat(loc.getLongtitude(), is(-118.4185002));
        assertThat(loc.getPropertyMap(), is("Doolan"));
    }

    @Test
    public void fieldsSetBySettersCanBeRead() {
        Location loc = new Location();
        loc.setId(654321);
        loc.setAddress("pool");
        loc.setLatitude(30.000001);
        loc.setLongtitude(-118.000001);
        loc.setPropertyMap("gym");
        assertThat(loc.getId(), is(654321));
        assertThat(loc.getAddress(), is("pool"));
        assertThat(loc.getLatitude(), is(30.000001));
        assertThat(loc.getLongtitude(), is(-118.000001));
        assertThat(loc.getPropertyMap(), is("gym"));
    }

    @Test
    public void equalsUsesIdAddressAndPropertymapOnly() {
        assertThat(new Location(1, "Keck Lab", "Doolan"), equalTo(new Location(1, "Keck Lab", "Doolan")));
        assertThat(new Location(1, "Keck Lab", "Doolan"), not(equalTo(new Location(1, "Restroom", "Doolan"))));
        assertThat(new Location(1, "Keck Lab", "Doolan"), not(equalTo(new Location(999, "Keck Lab", "Doolan"))));
        assertFalse(new Location(1, "Keck Lab", "Doolan").equals("candy"));
        assertFalse(new Location(1, "Keck Lab", "Doolan").equals(null));

    }

    @Test
    public void hashCodeProducesId() {
        assertThat(new Location(1, "Keck Lab", "Doolan").hashCode(), is(1));
    }
}