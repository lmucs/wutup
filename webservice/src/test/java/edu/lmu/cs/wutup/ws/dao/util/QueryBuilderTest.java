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
    public void queryWithOneSelectIsCorrect() {
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
        QueryBuilder query = new QueryBuilder().from("event").where("name = :name", "Rich");
        assertThat(query.build(), equalTo("select * from event where name = ?"));
        assertThat(query.getParametersArray()[0].toString(), equalTo("Rich"));
    }

    @Test
    public void queryWithTwoWheresIsCorrect() {
        QueryBuilder query = new QueryBuilder().from("event").where("name = :x", "Rich").where("radius = :y", "2");
        Object[] parameters = query.getParametersArray();
        assertThat(query.build(), equalTo("select * from event where name = ? and radius = ?"));
        assertThat(parameters[0].toString(), equalTo("Rich"));
        assertThat(parameters[1].toString(), equalTo("2"));

    }

    @Test
    public void queryWithWhereIntervalIsCorrect() {
        QueryBuilder query = new QueryBuilder().select("o.id", "o.start", "o.end", "o.venueId", "v.name as venueName",
                "v.address", "v.latitude", "v.longitude", "o.eventId", "e.name as eventName", "e.description",
                "address", "u.id as userId", "u.firstName", "u.lastName", "u.email", "u.nickname")
                .whereInterval(new Interval(new DateTime("2012-01-15T08:30:00"), new DateTime("2012-01-16T11:30:00")))
                .from("occurrence o")
                .joinOn("venue v", "o.venueId = v.id")
                .joinOn("event e", "o.eventId = e.id")
                .joinOn("user u", "e.ownerId = u.id");
        Object[] parameters = query.getParametersArray();
        assertThat(query.build(), equalTo("select o.id, o.start, o.end, o.venueId, v.name as venueName, v.address, "
                + "v.latitude, v.longitude, o.eventId, e.name as eventName, e.description, address, u.id as "
                + "userId, u.firstName, u.lastName, u.email, u.nickname from occurrence o join venue v on "
                + "(o.venueId = v.id) join event e on (o.eventId = e.id) join user u on (e.ownerId = u.id) where "
                + "start between ? and ? and end between ? and ?"));
        assertThat(parameters[0].toString(), equalTo("2012-01-15 08:30:00.0"));
        assertThat(parameters[1].toString(), equalTo("2012-01-16 11:30:00.0"));
        assertThat(parameters[2].toString(), equalTo("2012-01-15 08:30:00.0"));
        assertThat(parameters[3].toString(), equalTo("2012-01-16 11:30:00.0"));
    }

    @Test
    public void queryWithWhereCircleIsCorrect() {
        QueryBuilder query = new QueryBuilder().select("*")
                .whereCircle(new Circle(65, 120, 80))
                .from("occurrence o")
                .joinOn("venue v", "o.venueId = v.id")
                .joinOn("event e", "o.eventId = e.id")
                .joinOn("user u", "e.ownerId = u.id");
        assertThat(query.build(), equalTo("select * from occurrence o join venue v on (o.venueId = v.id) join "
                + "event e on (o.eventId = e.id) join user u on (e.ownerId = u.id) where "
                + "get_distance_miles(v.latitude, 65.0, v.longitude, 120.0) <= ?"));
        assertThat(query.getParametersArray()[0].toString(), equalTo("80.0"));
    }

    @Test
    public void queryWithWhereAndOrWhereIsCorrect() {
        QueryBuilder query = new QueryBuilder().from("event")
                .where("name = :name1", "ric")
                .orWhere("name = :name2", "part");
        Object[] parameters = query.getParametersArray();
        assertThat(query.build(), equalTo("select * from event where name = ? or name = ?"));
        assertThat(parameters[0].toString(), equalTo("ric"));
        assertThat(parameters[1].toString(), equalTo("part"));
    }

    @Test
    public void queryWithSingleLikeIsCorrect() {
        QueryBuilder query = new QueryBuilder().from("event").like("name", "name", "RIC");
        assertThat(query.build(), equalTo("select * from event where lower(name) like lower(?)"));
        assertThat(query.getParametersArray()[0].toString(), equalTo("%RIC%"));
    }

    @Test
    public void queryWithMultipleLikeIsCorrect() {
        QueryBuilder query = new QueryBuilder().from("event").like("name", "name", "RIC").like("name2", "name2", "h");
        Object[] parameters = query.getParametersArray();
        assertThat(query.build(),
                equalTo("select * from event where lower(name) like lower(?) and lower(name2) like lower(?)"));
        assertThat(parameters[0].toString(), equalTo("%RIC%"));
        assertThat(parameters[1].toString(), equalTo("%h%"));
    }

    @Test
    public void addPaginationHasCorrectLimitAndOffset() {
        String query = new QueryBuilder().from("event").addPagination(new PaginationData(0, 3)).build();
        assertThat(query, equalTo("select * from event limit 3 offset 0"));
    }

    @Test
    public void testQueryBuilderAppend() {
        QueryBuilder query = new QueryBuilder().from("event")
                .where("name = :userName", "Rich")
                .append(" some text to append");
        assertThat(query.build(), equalTo("select * from event where name = ? some text to append"));
        assertThat(query.getParametersArray()[0].toString(), equalTo("Rich"));

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
        QueryBuilder query = new QueryBuilder().from("event")
                .innerJoinOn("eventOccurrence", "event.id = eventOccurrence.id")
                .where("name = :name", "Rich");
        assertThat(
                query.build(),
                equalTo("select * from event inner join eventOccurrence on (event.id = eventOccurrence.id) where name = ?"));
        assertThat(query.getParametersArray()[0].toString(), equalTo("Rich"));
    }
}
