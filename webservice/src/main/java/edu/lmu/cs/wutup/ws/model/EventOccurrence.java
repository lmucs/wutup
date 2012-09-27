package edu.lmu.cs.wutup.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

import com.google.common.base.Objects;

@XmlRootElement(name = "eventOccurrence")
public class EventOccurrence {

    private Integer id;
    private String location;
    private DateTime start;
    private DateTime end;

    public EventOccurrence() {
        // No-arg constructor required for annotations
    }

    public EventOccurrence(int id, String location, DateTime start, DateTime end) {
        this.id = new Integer(id);
        this.location = location;
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

    public void setLocation(final String location) {
        this.location = location;
    }

    @XmlElement(name = "location")
    public String getLocation() {
        return this.location;
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
                .add("location", this.location)
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
            result = Objects.equal(id, other.id) && 
                    Objects.equal(location, other.location) &&
                    Objects.equal(this.start, other.start);
        }

        return result;
    }
}
