package edu.lmu.cs.wutup.ws.dao.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.lmu.cs.wutup.ws.model.PaginationData;

/**
 * QueryBuilder is a builder that constructs a SQL query from an initial string and any number of "clauses" or arbitrary
 * strings. Clauses represent conditions based on a particular value; the resulting appended string follows Hibernate's
 * format for supplying parameters (i.e., ":identifier").
 *
 *
 *
 * FIND_BY_ID_SQL = "select id, name from eventOccurrence where id=?"; FIND_ALL_SQL =
 * "select id, name from eventOccurrence limit ? offset ?";
 *
 * SELECT_COMMENT = "select ec.*, u.* from eventoccurence_comment ec join user u on (ec.authorId = u.id)"; private
 * static final String PAGINATION = "limit ? offset ?" FIND_COMMENTS_SQL = SELECT_COMMENT + " where ec.eventId = ? " +
 * PAGINATION;
 */
public class QueryBuilder {

    // Parameters must start with a lowercase ASCII letter.
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(:[a-z]\\w*)");
    private StringBuilder stringBuilder;
    private StringBuilder appendBuilder;
    private String select;
    private String from;
    private String order;
    private String pagination;
    private Map<String, Object> parameters = new HashMap<String, Object>();
    private Map<String, String> joinOns = new HashMap<String, String>();
    private List<String> clauses = new ArrayList<String>();
    private String queryString;

    /**
     * Produces a new query builder.
     */
    public QueryBuilder() {
        stringBuilder = new StringBuilder();
        appendBuilder = new StringBuilder();
        // No-arg constructor
    }

    /**
     * Throws an <code>IllegalStateException</code> if this builder has already built a query. Used to guard the build
     * operations.
     */
    private void assertNotBuilt() {
        if (queryString != null) {
            throw new IllegalStateException("The query has already been built");
        }
    }

    private void assertValidQuery() {
        if (from == null) {
            throw new IllegalStateException("The query does not have minimum query arguments");
        }
    }

    /**
     * Appends an arbitrary chunk of text to the query builder.
     */
    public QueryBuilder append(String text) {
        assertNotBuilt();
        appendBuilder.append(text);
        return this;
    }

    public QueryBuilder select(String fields) {
        assertNotBuilt();
        select = fields;
        return this;
    }

    public QueryBuilder from(String tableName) {
        assertNotBuilt();
        from = tableName;
        return this;
    }

    public QueryBuilder joinOn(String tableName, String joinCondition) {
        assertNotBuilt();
        joinOns.put(tableName, joinCondition);
        return this;
    }

    public QueryBuilder order(String newOrder) {
        assertNotBuilt();
        order = newOrder;
        return this;
    }

    /**
     * Adds an HQL clause to the list of clauses, finding at most one named parameter within the clause, and adding it
     * and its associated value to the parameter map. For example, calling <code>clause(":x > 5", 10)</code> will add
     * the clause ":x > 5" to clauses, and the mapping <code>["x" => 10]</code> to map.
     */
    public QueryBuilder where(String condition, Object paramValue) {
        assertNotBuilt();
        clauses.add(condition);
        Matcher matcher = PARAMETER_PATTERN.matcher(condition);
        if (matcher.find()) {
            parameters.put(matcher.group(1), paramValue);
        }
        return this;
    }

    public QueryBuilder addPagination(PaginationData p) {
        assertNotBuilt();
        pagination = "limit " + p.pageSize + " offset " + p.pageSize * p.pageNumber;
        return this;
    }

    /**
     * Puts the base string, the clauses, and the parameters all together into a Hibernate query object.
     *
     * This is a template method; it uses createQuery to instantiate the query object to be built and finishQuery to
     * perform any final operations before returning the query. Subclasses can override these methods as needed.
     */
    public String build() {
        assertValidQuery();
        stringBuilder.append("select " + (select != null ? select : "*")).append(" from " + from);

        if (!joinOns.isEmpty()) {
            for (Map.Entry<String, String> pairs : joinOns.entrySet()) {
                stringBuilder.append(" join " + pairs.getKey() + " on (" + pairs.getValue() + ")");
            }
        }

        boolean first = true;
        for (String clause : clauses) {
            stringBuilder.append(first ? " where " : " and ").append(clause);
            first = false;
        }

        if (order != null) {
            stringBuilder.append(" order by " + order);
        }

        for (Map.Entry<String, Object> e : parameters.entrySet()) {
            String parameterKey = e.getKey();
            int parameterLength = parameterKey.length();
            int parameterStartIndex = stringBuilder.indexOf(parameterKey);
            stringBuilder.replace(parameterStartIndex, parameterStartIndex + parameterLength, e.getValue().toString());
        }

        if (pagination != null) {
            stringBuilder.append(" " + pagination);
        }
        stringBuilder.append(appendBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * Returns the query string that has been built so far.
     */
    public String getQueryString() {
        return stringBuilder.toString();
    }

    /**
     * Retrieves the query parameters that have been added so far.
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    public static String formatForLikeStatement(String s) {
        return "\'" + s + "%\'";
    }

}
