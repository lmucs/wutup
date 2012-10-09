package edu.lmu.cs.wutup.ws.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import org.joda.time.DateTime;

public class EventOccurrenceTest {

    @Test
    public void fieldsSetByConstructorCanBeRead() {
        DateTime start = new DateTime(2012, 9, 27, 10, 50, 0);
        DateTime end = new DateTime(2012, 9, 27, 12, 5, 0);
        EventOccurrence e = new EventOccurrence(3, new Venue(3, "", "", 15.0, 15.0, null), start, end);
        // EventOccurrence e = new EventOccurrence(3, new Venue(3, "", "", 15.0, 15.0,""), start, end);
        assertThat(e.getId(), is(3));
        // assertThat(
        //         e.getVenue(),
        //         is("Venue{id=3, name=, address=, latitude=15.0, longitude=15.0}"));
        assertThat(e.getStart(), is(start));
        assertThat(e.getEnd(), is(end));
    }

    @Test
    public void fieldsSetBySettersCanBeRead() {
        EventOccurrence e = new EventOccurrence();
        DateTime start = new DateTime(2012, 9, 27, 9, 25, 0);
        DateTime end = new DateTime(2012, 9, 27, 10, 40, 0);
        e.setId(5);
        e.setVenue(new Venue(e.getId(), "","", 0.0, 100.0, null));
        // e.setVenue(new Venue(e.getId(), "", 0.0, 100.0, ""));
        e.setStart(start);
        e.setEnd(end);
        assertThat(e.getId(), is(5));
        //assertThat(
                //e.getVenue().toString(),
                //is("Venue{id=5, name=, address=, latitude=0.0, longitude=100.0}"));
                // is("Venue{id=5, address=, latitude=0.0, longitude=100.0, propertyMap=}"));
        assertThat(e.getStart(), is(start));
        assertThat(e.getEnd(), is(end));
    }

    @Test
    public void toStringProducesExpectedString() {
        DateTime start = new DateTime(2012, 9, 27, 9, 25, 0);
        DateTime end = new DateTime(2012, 9, 27, 10, 40, 0);
        EventOccurrence e1 = new EventOccurrence(3, new Venue(3, "", "", 33.969369, -118.414386, null), start, end);
        // EventOccurrence e1 = new EventOccurrence(3, new Venue(3, "", 33.969369, -118.414386, ""), start, end);
        String expected1 = "EventOccurrence{id=3,location=Venue{id=3, name=, address=, latitude=33.969369, longtitude=-118.414386}, "
                + "start=2012/09/27 09:25:00 AM, end=2012/09/27 10:40:00 AM}";
        EventOccurrence e2 = new EventOccurrence();
        e2.setId(5);
        e2.setStart(start);
        e2.setEnd(end);
        String expected2 = "EventOccurrence{id=5, location=null, "
                + "start=2012/09/27 09:25:00 AM, end=2012/09/27 10:40:00 AM}";
        // assertEquals(expected1, e1.toString());
        // assertEquals(expected2, e2.toString());
    }

    @Test
    public void hashCodeProducesId() {
        assertThat(new EventOccurrence(7, new Venue(), new DateTime(),
                new DateTime()).hashCode(), is(7));
    }

    @Test
    public void equalsUsesIdLocationAndStartOnly() {
        DateTime start1 = new DateTime(2011, 12, 25, 0, 0, 0);
        DateTime end1 = new DateTime(2011, 12, 25, 11, 59, 59);
        DateTime start2 = new DateTime(2011, 12, 25, 0, 0, 0);
        DateTime end2 = new DateTime(2011, 12, 25, 11, 59, 59);
        assertThat(new EventOccurrence(7, new Venue(), start1, end1),
                equalTo(new EventOccurrence(7, new Venue(), start2, end2)));
        assertThat(new EventOccurrence(7, new Venue(10, "", "", 5.0, 5.0, null),
                start1, end1), not(equalTo(new EventOccurrence(17, new Venue(
                10, "", "", 5.0, 5.0, null), new DateTime(), new DateTime()))));
        assertThat(new EventOccurrence(7, new Venue(10, "", "", 5.0, 5.0, null),
                new DateTime(), new DateTime()),
                not(equalTo(new EventOccurrence(7, new Venue(8, "","", 15.0, 5.0, null), new DateTime(), new DateTime()))));
        assertFalse(new EventOccurrence(7, new Venue(10, "", "", 5.0, 5.0, null),
                new DateTime(), new DateTime()).equals("some string"));
        assertFalse(new EventOccurrence(7, new Venue(10, "", "", 5.0, 5.0, null),
                new DateTime(), new DateTime()).equals(null));
    }

}
