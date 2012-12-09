package edu.lmu.cs.wutup.ws.resource;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import edu.lmu.cs.wutup.ws.exception.MalformedDateTimeStringException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.PaginationData;

/**
 * Base for all resource classes in the Wutup webservice.
 */
public abstract class AbstractWutupResource {

    private static final String EVENT_OCCURRENCE_QUERY_PARAMETERS_BAD = "Event occurrence query parameters improperly formed";
    private static final String PARAMETER_REQUIRED = "The parameter %s is required";
    private static final String PARAMETER_NON_INTEGER = "The parameter %s should be an integer";
    private static final String PARAMETER_OUT_OF_RANGE = "The parameter %s should be the range %d...%d";
    private static final String PATH_BODY_CONFLICT = "Id %d in path differs from id %d in body";
    private static final String MALFORMED_ARGUMENT_DATE = "The parameters %s and %s are not valid, respective start and end dates";
    private static final String MISSING_CENTER = "The center parameter must be present if the radius is supplied";
    private static final String MISSING_RADIUS = "The radius parameter must be present if the center is supplied";
    private static final String MALFORMED_PARAMETER = "The parameter '%s' is malformed";
    private static final String INSUFFICIENT_EVENT_DATA = "Not enough data to create event";
    private static final String INSUFFICIENT_OCCURRENCE_DATA = "Not enough data to create event occurrence";
    private static final String TIME_CANNOT_BE_PARSED = "The %s and %s parameters cannot be parsed into a valid DateTime";

    private static final Pattern CENTER_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?");
    private static final Pattern RADIUS_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");

    protected static final String DEFAULT_PAGE = "0";
    protected static final String DEFAULT_PAGE_SIZE = "20";

    Logger logger = Logger.getLogger(getClass());

    /**
     * Throws a service exception with BAD_REQUEST is the value is null (corresponds to a missing required HTTP
     * parameter.
     */
    void checkRequiredParameter(String name, String value) {
        if (value == null) {
            throw new ServiceException(BAD_REQUEST, PARAMETER_REQUIRED, name);
        }
    }

    /**
     * Throws a service exception with BAD_REQUEST if the parameter is not readable as an integer (if present).
     */
    Integer toInteger(String name, String value) {
        if (value == null) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ServiceException(BAD_REQUEST, PARAMETER_NON_INTEGER, name);
        }
    }

    /**
     * Throws a service exception with BAD_REQUEST if the parameter is missing or not readable as an integer.
     */
    Integer toIntegerRequired(String name, String value) {
        checkRequiredParameter(name, value);
        return toInteger(name, value);
    }

    /**
     * Throws a service exception with FORBIDDEN if the given value is not in the range low..high.
     */
    void checkRange(String name, int value, int low, int high) {
        if (value < low || value > high) {
            throw new ServiceException(FORBIDDEN, PARAMETER_OUT_OF_RANGE, name, low, high);
        }
    }

    /**
     * Throws a service exception with BAD_REQUEST if the parameter value doesn't match the regex.
     */
    void checkParameterSyntax(String parameterName, Pattern regex, String value) {
        Matcher matcher = regex.matcher(value);
        if (!matcher.matches()) {
            throw new ServiceException(BAD_REQUEST, MALFORMED_PARAMETER, parameterName);
        }
    }

    /**
     * Throws a service exception with CONFLICT if the two ids are both present but not the same.
     */
    void checkIdAgreement(int idInPath, Integer idInBody) {
        if (idInBody != null && idInPath != idInBody) {
            throw new ServiceException(CONFLICT, PATH_BODY_CONFLICT, idInPath, idInBody);
        }
    }

    /**
     * @throws ServiceException
     *             with BAD_REQUEST if creator or name are not specified.
     */
    void checkEventCanBeCreated(Event e) {
        if (e.getCreator() == null || e.getCreator().getId() == null || e.getName() == null) {
            throw new ServiceException(BAD_REQUEST, INSUFFICIENT_EVENT_DATA);
        }
    }

    /**
     * @throws ServiceException
     *             with BAD_REQUEST if eventOccurrence is insufficiently specified.
     */
    void checkOccurrenceCanBeCreated(EventOccurrence o) {
        if (o.getEvent() == null || o.getEvent().getId() == null || o.getVenue() == null || o.getVenue().getId() == null) {
            throw new ServiceException(BAD_REQUEST, INSUFFICIENT_OCCURRENCE_DATA);
        }
    }

    void checkOccurrenceCanBeQueried(Integer attendee, Circle circle, Interval interval, List<Integer> eventId,
            Integer venueId) {
        if (attendee == null && circle == null && interval == null && eventId == null && venueId == null) {
            throw new ServiceException(BAD_REQUEST, EVENT_OCCURRENCE_QUERY_PARAMETERS_BAD);
        }
    }

    /**
     * Creates a circle object out of strings hopefully in the official format of the API; throws a service exception
     * with BAD_REQUEST if the strings are malformed or if only partially specified; throws a service exception with
     * FORBIDDEN if values are out of range.
     */
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

        try {
            return new Circle(centerLatitude, centerLongitude, radius);
        } catch (IllegalArgumentException e) {
            throw new ServiceException(FORBIDDEN, e.getMessage());
        }
    }

    /**
     * Creates an interval object out of strings that will be parsed as longs into DateTimes; throws a service exception
     * with BAD_REQUEST if the strings are malformed or if only partially specified.
     *
     * @throws ServiceException
     *             with BAD_REQUEST if interval cannot be created.
     */
    Interval makeIntervalFromStartAndEndTime(String start, String end) {
        if (start == null && end == null) {
            return null;
        }
        if (start == null || end == null) {
            throw new ServiceException(BAD_REQUEST, MALFORMED_ARGUMENT_DATE, "end time", "start time");
        }

        try {
            DateTime startTime = new DateTime(Long.parseLong(start));
            DateTime endTime = new DateTime(Long.parseLong(end));
            return new Interval(startTime, endTime);
        } catch (MalformedDateTimeStringException e) {
            throw new ServiceException(BAD_REQUEST, TIME_CANNOT_BE_PARSED);
        } catch (NumberFormatException e) {
            throw new ServiceException(BAD_REQUEST, TIME_CANNOT_BE_PARSED);
        } catch (IllegalArgumentException e) {
            throw new ServiceException(BAD_REQUEST, TIME_CANNOT_BE_PARSED);
        }
    }

    /**
     * Creates a pagination data object out of string parameters. Throws a service exception with BAD_REQUEST if either
     * parameter is missing or malformed; throws a service exception with FORBIDDEN if values are out of range.
     */
    PaginationData paginationDataFor(String pageString, String pageSizeString) {

        int page = toIntegerRequired("page", pageString);
        int pageSize = toIntegerRequired("pageSize", pageSizeString);

        try {
            return new PaginationData(page, pageSize);
        } catch (IllegalArgumentException e) {
            throw new ServiceException(FORBIDDEN, e.getMessage());
        }
    }
}
