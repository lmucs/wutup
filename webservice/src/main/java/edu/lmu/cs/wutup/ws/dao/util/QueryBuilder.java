package edu.lmu.cs.wutup.ws.dao.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.joda.time.Interval;

import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.PaginationData;

/**
 * QueryBuilder is a builder that constructs a SQL query. Where clauses represent conditions based on a particular
 * value; the resulting appended string follows Hibernate's format for supplying parameters (i.e., ":identifier"). The
 * functionality of this class is similar to that of Querydsl.
 */
public class QueryBuilder {

    private Logger logger = Logger.getLogger(getClass());
    // Parameters must start with a lowercase ASCII letter.
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("(:[a-z]\\w*)");
    private StringBuilder stringBuilder;
    private StringBuilder appendBuilder;
    private String select;
    private String from;
    private String order;
    private String pagination;
    private Map<String, Object> andParameters = new LinkedHashMap<String, Object>();
    private Map<String, Object> orParameters = new LinkedHashMap<String, Object>();
    private MultiValueMap joinByTypes = new MultiValueMap();
    private List<String> andClauses = new ArrayList<String>();
    private List<String> orClauses = new ArrayList<String>();
    private String queryString;

    /**
     * Produces a new query builder.
     */
    public QueryBuilder() {
        stringBuilder = new StringBuilder();
        appendBuilder = new StringBuilder();
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

    public QueryBuilder select(String... fields) {
        assertNotBuilt();
        select = "";
        for (String field : fields) {
            select += field + ", ";
        }
        select = select.substring(0, select.length() - 2);
        return this;
    }

    public QueryBuilder from(String tableName) {
        assertNotBuilt();
        from = tableName;
        return this;
    }

    private void addJoin(String type, String tableName, String joinCondition) {
        assertNotBuilt();
        if (type != null && tableName != null && joinCondition != null) {
            joinByTypes.put(type, tableName);
            joinByTypes.put(type, joinCondition);
        }
    }

    public QueryBuilder joinOn(String tableName, String joinCondition) {
        addJoin("join", tableName, joinCondition);
        return this;
    }

    public QueryBuilder innerJoinOn(String tableName, String joinCondition) {
        addJoin("inner join", tableName, joinCondition);
        return this;
    }

    public QueryBuilder order(String order) {
        assertNotBuilt();
        this.order = order;
        return this;
    }

    public QueryBuilder orWhere(String condition, Object paramValue) {
        assertNotBuilt();
        if (paramValue != null) {
            String paramValueString = paramValue.toString();
            Matcher matcher = PARAMETER_PATTERN.matcher(condition);
            if (matcher.find()) {
                orParameters.put(matcher.group(1), paramValueString);
            }
            condition = condition.replace(matcher.group(1), "?");
            orClauses.add(condition);
        }
        return this;
    }

    /**
     * Adds a clause to the list of clauses, finding at most one named parameter within the clause, and adding it and
     * its associated value to the parameter map. For example, calling <code>clause(":x > 5", 10)</code> will add the
     * clause ":x > 5" to clauses, and the mapping <code>["x" => 10]</code> to map.
     */
    public QueryBuilder where(String condition, Object paramValue) {
        assertNotBuilt();
        if (paramValue != null) {
            String paramValueString = paramValue.toString();
            Matcher matcher = PARAMETER_PATTERN.matcher(condition);
            if (matcher.find()) {
                andParameters.put(matcher.group(1), paramValueString);
            }
            condition = condition.replace(matcher.group(1), "?");
            andClauses.add(condition);
        }
        return this;
    }

    public QueryBuilder whereCircle(Circle c) {
        if (c != null) {
            return this.where("get_distance_miles(v.latitude, " + c.centerLatitude + ", v.longitude, "
                    + c.centerLongitude + ") <= :radius", c.radius);
        } else {
            return this;
        }
    }

    public QueryBuilder whereInterval(Interval i) {
        assertNotBuilt();
        if (i != null) {
            Timestamp startStamp = new Timestamp(i.getStartMillis());
            Timestamp endStamp = new Timestamp(i.getEndMillis());

            andClauses.add("start between ? and ?");
            andClauses.add("end between ? and ?");
            andParameters.put(":start1", startStamp);
            andParameters.put(":end1", endStamp);
            andParameters.put(":start2", startStamp);
            andParameters.put(":end2", endStamp);
        }
        return this;
    }

    public QueryBuilder like(String field, String paramName, Object paramValue) {
        return this.where("lower(" + field + ") like lower(:" + paramName + ")", "%" + paramValue.toString() + "%");
    }

    public QueryBuilder addPagination(PaginationData p) {
        assertNotBuilt();
        if (p != null) {
            pagination = "limit " + p.pageSize + " offset " + p.pageSize * p.pageNumber;
        }
        return this;
    }

    /**
     * Puts the base string, the clauses, and the parameters all together into a query object.
     */
    @SuppressWarnings("unchecked")
    public String build() {
        assertValidQuery();
        stringBuilder.append("select " + (select != null ? select : "*")).append(" from " + from);

        if (!joinByTypes.isEmpty()) {
            // TODO: Ask for help correcting type-safety warnings below
            Set<String> keySet = joinByTypes.keySet();
            for (Object key : keySet) {
                ArrayList<String> a = (ArrayList<String>) joinByTypes.getCollection(key);
                for (int i = 0; i < a.size(); i += 2) {
                    stringBuilder.append(" " + key + " " + a.get(i) + " on (" + a.get(i + 1) + ")");
                }
            }
        }

        boolean first = true;
        for (String clause : andClauses) {
            stringBuilder.append(first ? " where " : " and ").append(clause);
            first = false;
        }
        for (String clause : orClauses) {
            stringBuilder.append(first ? " where " : " or ").append(clause);
            first = false;
        }

        if (order != null) {
            stringBuilder.append(" order by " + order);
        }

        if (pagination != null) {
            stringBuilder.append(" " + pagination);
        }
        stringBuilder.append(appendBuilder.toString());
        logger.info("QUERY: " + getQueryString() + " PARAMS: " + getParameters());
        return getQueryString();
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
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.putAll(andParameters);
        parameters.putAll(orParameters);
        return parameters;
    }

    public Object[] getParametersArray() {
        return ArrayUtils.addAll(andParameters.values().toArray(), orParameters.values().toArray());
    }
}
