package edu.lmu.cs.wutup.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

import com.google.common.base.Objects;

@XmlRootElement(name = "eventOccurrence")
public class EventOccurrence {

    private Integer id;
    private Venue venue;
    private DateTime start;
    private DateTime end;

    public EventOccurrence() {
        // toString gives errors if DateTime properties are null,
        // Initializing new DateTime should set start and end time
        // as when initialized. Not sure if the best way to handle.

        // No-arg constructor required for annotations
    }

    public EventOccurrence(final int id, final Venue venue) {
        this(id, venue, new DateTime(), new DateTime());
    }

    public EventOccurrence(int id, Venue venue, DateTime start, DateTime end) {
        this.id = new Integer(id);
        this.venue = venue;
        this.start = start;
        this.end = end;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name = "id")
    public int getId() {
        return this.id;
    }

    public void setVenue(final Venue venue) {
        // Setting address and property map as empty strings until I better
        // understand what to keep within these properties.
        this.venue = venue;
    }

    @XmlElement(name = "venue")
    public String getVenue() {
        return this.venue.toString();
    }

    public void setStart(final DateTime start) {
        this.start = start;
    }

    @XmlElement(name = "start")
    public DateTime getStart() {
        return this.start;
    }

    public void setEnd(final DateTime end) {
        this.end = end;
    }

    @XmlElement(name = "end")
    public DateTime getEnd() {
        return this.end;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("id", this.id)
                .add("location", this.venue)
                .add("start", this.start.toString("yyyy/MM/dd hh:mm:ss aa"))
                .add("end", this.end.toString("yyyy/MM/dd hh:mm:ss aa"))
                .toString();
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj != null && obj instanceof EventOccurrence) {
            EventOccurrence other = EventOccurrence.class.cast(obj);
            result = Objects.equal(id, other.id)
                    && Objects.equal(venue, other.venue)
                    && Objects.equal(this.start, other.start);
        }

        return result;
    }
}
