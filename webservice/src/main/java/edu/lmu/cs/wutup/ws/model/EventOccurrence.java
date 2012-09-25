package edu.lmu.cs.wutup.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement(name = "eventOccurrence")
public class EventOccurrence {

    private Integer id;


    public EventOccurrence() {
        // No-arg constructor required for annotations
    }

    public EventOccurrence(int id) {
        this.id = new Integer(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name = "id")
    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("id", this.id).toString();
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
            result = Objects.equal(id, other.id);
        }

        return result;
    }
}
