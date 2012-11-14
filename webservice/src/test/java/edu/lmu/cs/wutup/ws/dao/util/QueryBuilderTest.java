package edu.lmu.cs.wutup.ws.dao.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.PaginationData;

public class QueryBuilderTest {

    @Test
    public void queryWithoutSelectIsCorrect() {
        String query = new QueryBuilder().from("event").build();
        assertThat(query, equalTo("select * from event"));
    }

    @Test
    public void queryWithSpecificSelectIsCorrect() {
        String query = new QueryBuilder().select("owner").from("event").build();
        assertThat(query, equalTo("select owner from event"));
    }

    @Test
    public void queryWithMultipleSelectIsCorrect() {
        String query = new QueryBuilder().select("owner", "name", "description").from("event").build();
        assertThat(query, equalTo("select owner, name, description from event"));
    }

    @Test
    public void queryWithSingleWhereIsCorrect() {
        String query = new QueryBuilder().from("event").where("name = :name", "'Rich'").build();
        assertThat(query, equalTo("select * from event where name = 'Rich'"));
    }

    @Test
    public void queryWithTwoWheresIsCorrect() {
        String query = new QueryBuilder().from("event").where("name = :x", "'Rich'").where("radius = :y", "2").build();
        assertThat(query, equalTo("select * from event where name = 'Rich' and radius = 2"));
    }

    @Test
    public void queryWithWhereIntervalIsCorrect() {
        String query = new QueryBuilder().select("o.id", "o.start", "o.end", "o.venueId", "v.name as venueName",
                "v.address", "v.latitude", "v.longitude", "o.eventId", "e.name as eventName", "e.description",
                "address", "u.id as userId", "u.firstName", "u.lastName", "u.email", "u.nickname")
                .whereInterval(new Interval(new DateTime("2012-01-15T08:30:00"), new DateTime("2012-01-16T11:30:00")))
                .from("occurrence o")
                .joinOn("venue v", "o.venueId = v.id")
                .joinOn("event e", "o.eventId = e.id")
                .joinOn("user u", "e.ownerId = u.id")
                .build();
        assertThat(query, equalTo("select o.id, o.start, o.end, o.venueId, v.name as venueName, v.address, " +
                "v.latitude, v.longitude, o.eventId, e.name as eventName, e.description, address, u.id as " +
                "userId, u.firstName, u.lastName, u.email, u.nickname from occurrence o join venue v on " +
                "(o.venueId = v.id) join event e on (o.eventId = e.id) join user u on (e.ownerId = u.id) where " +
                "start between '2012-01-15 08:30:00.0' and '2012-01-16 11:30:00.0' and end between " +
                "'2012-01-15 08:30:00.0' and '2012-01-16 11:30:00.0'"));
    }

    @Test
    public void queryWithWhereCircleIsCorrect() {
        String query = new QueryBuilder().select("*")
                .whereCircle(new Circle(65, 120, 80))
                .from("occurrence o")
                .joinOn("venue v", "o.venueId = v.id")
                .joinOn("event e", "o.eventId = e.id")
                .joinOn("user u", "e.ownerId = u.id")
                .build();
        assertThat(query, equalTo("select * from occurrence o join venue v on (o.venueId = v.id) join " +
                "event e on (o.eventId = e.id) join user u on (e.ownerId = u.id) where " +
                "get_distance_miles(v.latitude, 65.0, v.longitude, 120.0) <= 80.0"));
    }

    @Test
    public void addPaginationHasCorrectLimitAndOffset() {
        String query = new QueryBuilder().from("event").addPagination(new PaginationData(0, 3)).build();
        assertThat(query, equalTo("select * from event limit 3 offset 0"));
    }

    @Test
    public void testQueryBuilderAppend() {
        String query = new QueryBuilder().from("event")
                .where("name = :userName", "'Rich'")
                .append(" some text to append")
                .build();
        assertThat(query, equalTo("select * from event where name = 'Rich' some text to append"));
    }

    @Test
    public void testQueryWithJoin() {
        String query = new QueryBuilder().from("event")
                .joinOn("eventOccurrence", "event.id = eventOccurrence.id")
                .build();
        assertThat(query, equalTo("select * from event join eventOccurrence on (event.id = eventOccurrence.id)"));
    }

    @Test
    public void testQueryWithInnerJoinAndSingleWhere() {
        String query = new QueryBuilder().from("event")
                .innerJoinOn("eventOccurrence", "event.id = eventOccurrence.id")
                .where("name = :name", "'Rich'")
                .build();
        assertThat(
                query,
                equalTo("select * from event inner join eventOccurrence on (event.id = eventOccurrence.id) where name = 'Rich'"));
    }
}
