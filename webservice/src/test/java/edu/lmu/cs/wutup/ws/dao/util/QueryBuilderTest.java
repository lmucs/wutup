package edu.lmu.cs.wutup.ws.dao.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.model.PaginationData;

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
    
    @Test
    public void addPaginationHasCorrectLimitAndOffset() {
        String query = new QueryBuilder("select * from event")
                .addPagination(new PaginationData(0, 3))
                .build();
        assertThat(query, equalTo("select * from event limit 3 offset 0"));
    }
}
