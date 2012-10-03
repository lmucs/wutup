package edu.lmu.cs.wutup.ws.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class VenueTest {

    @Test
    public void fieldsSetByConstructorCanBeRead() {
        Venue loc = new Venue(123456, "Keck Lab", "1234 main st", 33.969962, -118.4185002);
        // Venue loc = new Venue(123456, "Keck Lab", "1234 main st", 33.969962, -118.4185002, "Doolan");
        assertThat(loc.getId(), is(123456));
        assertThat(loc.getName(), is("Keck Lab"));
        assertThat(loc.getAddress(), is("1234 main st"));
        assertThat(loc.getLatitude(), is(33.969962));
        assertThat(loc.getLongtitude(), is(-118.4185002));
        // assertThat(loc.getPropertyMap(), is("Doolan"));
    }

    @Test
    public void fieldsSetBySettersCanBeRead() {
        Venue loc = new Venue();
        loc.setId(654321);
        loc.setName("LMU pool");
        loc.setAddress("pool");
        loc.setLatitude(30.000001);
        loc.setLongtitude(-118.000001);
        // loc.setPropertyMap("gym");
        assertThat(loc.getId(), is(654321));
        assertThat(loc.getName(), is("LMU pool"));
        assertThat(loc.getAddress(), is("pool"));
        assertThat(loc.getLatitude(), is(30.000001));
        assertThat(loc.getLongtitude(), is(-118.000001));
        // assertThat(loc.getPropertyMap(), is("gym"));
    }

    @Test
    public void equalsUsesIdAddressAndPropertymapOnly() {
        assertThat(new Venue(1, "Keck Lab", "123 main"), is(new Venue(1, "Keck Lab", "123 main")));
        assertThat(new Venue(1, "Keck Lab", "123 main"), not(is(new Venue(1, "Restroom", "456 maple"))));
        assertThat(new Venue(1, "Keck Lab", "123 main"), not(is(new Venue(999, "Keck Lab", "123 main"))));
        assertFalse(new Venue(1, "Keck Lab", "123 main").equals("candy"));
        assertFalse(new Venue(1, "Keck Lab", "123 main").equals(null));
    }

    @Test
    public void hashCodeProducesId() {
        assertThat(new Venue(1, "Keck Lab", "123 main").hashCode(), is(1));
    }
}