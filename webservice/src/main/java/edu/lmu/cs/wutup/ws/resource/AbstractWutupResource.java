package edu.lmu.cs.wutup.ws.resource;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.Circle;

/**
 * Base for all resource classes in the Wutup webservice.
 */
public abstract class AbstractWutupResource {

    private static final String PARAMETER_REQUIRED = "The parameter %s is required";
    private static final String PARAMETER_NON_INTEGER = "The parameter %s should be an integer";
    private static final String PARAMETER_OUT_OF_RANGE = "The parameter %s should be the range %d...%d";
    private static final String PATH_BODY_CONFLICT = "Id %d in path differs from id %d in body";
    private static final String MALFORMED_ARGUMENT_DATE = "The parameters %s and %s are not valid, respective start and end dates";
    private static final String MISSING_CENTER = "The center parameter must be present if the radius is supplied";
    private static final String MISSING_RADIUS = "The radius parameter must be present if the center is supplied";
    private static final String MALFORMED_PARAMETER = "The parameter '%s' is malformed";

    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 50;
    private static final double MIN_SEARCH_RADIUS = 0.001;
    private static final double MAX_SEARCH_RADIUS = 200;

    private static final Pattern CENTER_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?");
    private static final Pattern RADIUS_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");

    /**
     * Ultimately causes an HTTP 400 (Bad Request) if the value is null (corresponds to a missing required HTTP
     * parameter.
     */
    void checkRequiredParameter(String name, String value) {
        if (value == null) {
            throw new ServiceException(BAD_REQUEST, PARAMETER_REQUIRED, name);
        }
    }

    /**
     * Returns the integer value for an HTTP parameter, ultimately causing an HTTP 400 (Bad Request) if the parameter
     * value is not readable as an integer.
     */
    int toInteger(String name, String value) {
        checkRequiredParameter(name, value);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ServiceException(BAD_REQUEST, PARAMETER_NON_INTEGER, name);
        }

    }

    /**
     * Ultimately causes an HTTP 403 (Forbidden) if the value is out of range (corresponds to an HTTP request asking for
     * something that is too small or too large.
     */
    void checkDoubleRange(String name, double value, double low, double high) {
        if (value < low || value > high) {
            throw new ServiceException(FORBIDDEN, PARAMETER_OUT_OF_RANGE, name, low, high);
        }
    }

    /**
     * Ultimately causes an HTTP 403 (Forbidden) if the value is out of range (corresponds to an HTTP request asking for
     * something that is too small or too large.
     */
    void checkRange(String name, int value, int low, int high) {
        if (value < low || value > high) {
            throw new ServiceException(FORBIDDEN, PARAMETER_OUT_OF_RANGE, name, low, high);
        }
    }

    void checkPageSizeRange(int value) {
        checkRange("pageSize", value, MIN_PAGE_SIZE, MAX_PAGE_SIZE);
    }

    /**
     * Ultimately generates an HTTP 400 if the parameter value doesn't match the regex.
     */
    void checkParameterSyntax(String parameterName, Pattern regex, String value) {
        Matcher matcher = regex.matcher(value);
        if (!matcher.matches()) {
            throw new ServiceException(BAD_REQUEST, MALFORMED_PARAMETER, parameterName);
        }
    }

    /**
     * Ultimately causes an HTTP 409 (Conflict) if the two ids are not the same.
     */
    void checkIdAgreement(int idInPath, Integer idInBody) {
        if (idInBody != null && idInPath != idInBody) {
            throw new ServiceException(CONFLICT, PATH_BODY_CONFLICT, idInPath, idInBody);
        }
    }

    /**
     * Utility method for checking an input interval's validity.
     */
    Interval validateInterval(DateTime startDate, DateTime endDate) {
        try {
            return new Interval(startDate, endDate);
        } catch (IllegalArgumentException iae) {
            throw new ServiceException(BAD_REQUEST, MALFORMED_ARGUMENT_DATE, startDate, endDate);
        }
    }

    Circle fromCenterAndRadiusParameters(String center, String radiusString) {
        if (center == null && radiusString == null) {
            // Perfectly fine, these are optional, but must appear together
            return null;
        }

        if (center != null && radiusString == null) {
            throw new ServiceException(BAD_REQUEST, MISSING_RADIUS);
        }

        if (center == null && radiusString != null) {
            throw new ServiceException(BAD_REQUEST, MISSING_CENTER);
        }

        checkParameterSyntax("center", CENTER_PATTERN, center);
        checkParameterSyntax("radius", RADIUS_PATTERN, radiusString);
        String[] coordinates = center.split(",");
        double centerLatitude = Double.parseDouble(coordinates[0]);
        double centerLongitude = Double.parseDouble(coordinates[1]);
        double radius = Double.parseDouble(radiusString);
        checkDoubleRange("radius", radius, MIN_SEARCH_RADIUS, MAX_SEARCH_RADIUS);
        return new Circle(centerLatitude, centerLongitude, radius);
    }
}
