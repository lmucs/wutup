package edu.lmu.cs.wutup.ws.model;

import com.google.common.base.Preconditions;

/**
 * A container for pagination data.
 */
public class PaginationData {

    private static double MAX_PAGE_SIZE = 50;

    private static final String BAD_PAGE_NUMBER = "Page number out of range: %s";
    private static final String BAD_PAGE_SIZE = "Page size out of range: %s";

    public int pageNumber;
    public int pageSize;

    public PaginationData(int pageNumber, int pageSize) {

        Preconditions.checkArgument(pageNumber >= 0, BAD_PAGE_NUMBER, pageNumber);
        Preconditions.checkArgument(pageSize > 0 && pageSize <= MAX_PAGE_SIZE, BAD_PAGE_SIZE, pageSize);

        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}
