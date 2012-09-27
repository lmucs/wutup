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
        EventOccurrence e = new EventOccurrence(3, "latitude and longitude",
                start, end);
        assertThat(e.getId(), is(3));
        assertThat(e.getLocation(), is("latitude and longitude"));
        assertThat(e.getStart(), is(start));
        assertThat(e.getEnd(), is(end));
    }

    @Test
    public void fieldsSetBySettersCanBeRead() {
        EventOccurrence e = new EventOccurrence();
        DateTime start = new DateTime(2012, 9, 27, 9, 25, 0);
        DateTime end = new DateTime(2012, 9, 27, 10, 40, 0);
        e.setId(5);
        e.setLocation("latitude and longitude again");
        e.setStart(start);
        e.setEnd(end);
        assertThat(e.getId(), is(5));
        assertThat(e.getLocation(), is("latitude and longitude again"));
        assertThat(e.getStart(), is(start));
        assertThat(e.getEnd(), is(end));
    }

    @Test
    public void toStringProducesExpectedString() {
        DateTime start = new DateTime(2012, 9, 27, 9, 25, 0);
        DateTime end = new DateTime(2012, 9, 27, 10, 40, 0);
        EventOccurrence e1 = new EventOccurrence(3, "latitude and longitude",
                start, end);
        String expected1 = "EventOccurrence{id=3, location=latitude and longitude, "
                + "start=2012/09/27 09:25:00 AM, end=2012/09/27 10:40:00 AM}";
        EventOccurrence e2 = new EventOccurrence();
        e2.setId(5);
        e2.setStart(start);
        e2.setEnd(end);
        String expected2 = "EventOccurrence{id=5, location=null, "
                + "start=2012/09/27 09:25:00 AM, end=2012/09/27 10:40:00 AM}";
        assertEquals(expected1, e1.toString());
        assertEquals(expected2, e2.toString());
    }

    @Test
    public void hashCodeProducesId() {
        assertThat(new EventOccurrence(7, "", new DateTime(),
                new DateTime()).hashCode(), is(7));
    }

    @Test
    public void equalsUsesIdLocationAndStartOnly() {
        DateTime start1 = new DateTime(2011, 12, 25, 0, 0, 0);
        DateTime end1 = new DateTime(2011, 12, 25, 11, 59, 59);
        DateTime start2 = new DateTime(2011, 12, 25, 0, 0, 0);
        DateTime end2 = new DateTime(2011, 12, 25, 11, 59, 59);
        assertThat(new EventOccurrence(7, "Location", start1, end1),
                equalTo(new EventOccurrence(7,
                "Location", start2, end2)));
        assertThat(new EventOccurrence(7, "Location", start1, end1),
                not(equalTo(new EventOccurrence(17,
                "Location", new DateTime(), new DateTime()))));
        assertThat(new EventOccurrence(7, "Location", new DateTime(), new DateTime()),
                not(equalTo(new EventOccurrence(7,
                "other location", new DateTime(), new DateTime()))));
        assertFalse(new EventOccurrence(7, "Pool Party", new DateTime(), new DateTime()).equals("some string"));
        assertFalse(new EventOccurrence(7, "Pool Party", new DateTime(), new DateTime()).equals(null));
    }

}
