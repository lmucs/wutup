package edu.lmu.cs.wutup.ws.dao.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

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
    public void queryWithSingleWhereIsCorrect() {
        String query = new QueryBuilder().from("event").where("name = :name", "'Rich'").build();
        assertThat(query, equalTo("select * from event where name = 'Rich'"));
    }

    @Test
    public void queryWithTwoWheresIsCorrect() {
        String query = new QueryBuilder().from("event")
                .where("name = :x", "'Rich'")
                .where("radius = :y", "2")
                .build();
        assertThat(query, equalTo("select * from event where name = 'Rich' and radius = 2"));
    }

    @Test
    public void addPaginationHasCorrectLimitAndOffset() {
        String query = new QueryBuilder().from("event").addPagination(new PaginationData(0, 3)).build();
        assertThat(query, equalTo("select * from event limit 3 offset 0"));
    }

    @Test
    public void testQueryBuilderAppend() {
        String query = new QueryBuilder().from("event").where("name = :userName", "'Rich'")
                .append(" some text to append")
                .build();
        assertThat(query, equalTo("select * from event where name = 'Rich' some text to append"));
    }
}
