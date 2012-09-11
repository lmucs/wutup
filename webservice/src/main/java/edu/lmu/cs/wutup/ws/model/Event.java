package edu.lmu.cs.wutup.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement(name = "event")
public class Event {

    private Integer id;
    private String name;

    public Event() {
        // No-arg constructor required for annotations
    }

    public Event(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name = "id")
    public int getId() {
        return this.id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @XmlElement(name = "name")
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("id", this.id).add("name", this.name).toString();
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj != null && obj instanceof Event) {
            Event other = (Event) obj;
            result = other.id == this.id && Objects.equal(other.name, this.name);
        }

        return result;
    }
}
