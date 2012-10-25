package edu.lmu.cs.wutup.ws.dao.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class QueryBuilderTest {

    @Test
    public void queryWithoutClausesIsCorrect() {
        String query = new QueryBuilder("select * from event").build();
        assertThat(query, equalTo("select * from event"));
    }

    @Test
    public void queryWithSingleClauseIsCorrect() {
        String query = new QueryBuilder("select * from event")
                .clause("name = :x", "'Rich'")
                .build();
        assertThat(query, equalTo("select * from event where name = 'Rich'"));
    }

    @Test
    public void queryWithTwoClausesIsCorrect() {
        String query = new QueryBuilder("select * from event")
                .clause("name = :x", "'Rich'")
                .clause("radius = :y", "2")
                .build();
        assertThat(query, equalTo("select * from event where name = 'Rich' and radius = 2"));
    }
}
