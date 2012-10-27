package edu.lmu.cs.wutup.ws.model;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.equalTo;

public class PaginationDataTest {

    @Test
    public void constructorSetsFieldsProperly() {
        PaginationData p = new PaginationData(2, 10);
        assertThat(p.pageNumber, equalTo(2));
        assertThat(p.pageSize, equalTo(10));
    }

    @Test
    public void constructorWorksAtBounds() {
        new PaginationData(0, 1);
        new PaginationData(0, 50);
    }

    @Test
    public void pageNumberTooLowIsChecked() {
        boundsCheck(-1, 5, "Page number out of range: -1");
    }

    @Test
    public void pageSizeTooLowIsChecked() {
        boundsCheck(4, 0, "Page size out of range: 0");
    }

    @Test
    public void pageSizeTooHighIsChecked() {
        boundsCheck(0, 500, "Page size out of range: 500");
    }

    private void boundsCheck(int pageNumber, int pageSize, String expectedMessage) {
        try {
            new PaginationData(pageNumber, pageSize);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo(expectedMessage));
        }
    }
}
