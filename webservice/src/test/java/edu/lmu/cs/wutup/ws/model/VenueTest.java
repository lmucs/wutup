package edu.lmu.cs.wutup.ws.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class VenueTest {

    @Test
    public void fieldsSetByConstructorCanBeRead() {
        Venue loc = new Venue(123456, "Keck Lab", 33.969962, -118.4185002, "Doolan");
        assertThat(loc.getId(), is(123456));
        assertThat(loc.getAddress(), is("Keck Lab"));
        assertThat(loc.getLatitude(), is(33.969962));
        assertThat(loc.getLongtitude(), is(-118.4185002));
        assertThat(loc.getPropertyMap(), is("Doolan"));
    }

    @Test
    public void fieldsSetBySettersCanBeRead() {
        Venue loc = new Venue();
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
        assertThat(new Venue(1, "Keck Lab"), is(new Venue(1, "Keck Lab")));
        assertThat(new Venue(1, "Keck Lab"), not(is(new Venue(1, "Restroom"))));
        assertThat(new Venue(1, "Keck Lab"), not(is(new Venue(999, "Keck Lab"))));
        assertFalse(new Venue(1, "Keck Lab").equals("candy"));
        assertFalse(new Venue(1, "Keck Lab").equals(null));

    }

    @Test
    public void hashCodeProducesId() {
        assertThat(new Venue(1, "Keck Lab").hashCode(), is(1));
    }
}