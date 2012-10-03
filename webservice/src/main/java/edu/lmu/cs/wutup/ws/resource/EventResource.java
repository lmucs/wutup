package edu.lmu.cs.wutup.ws.resource;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edu.lmu.cs.wutup.ws.exception.EventExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.service.EventService;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Path("/events")
public class EventResource {

    private static final String EVENT_NOT_FOUND = "Event %d does not exist";
    private static final String EVENT_ALREADY_EXISTS = "Event %d already exists";
    private static final String PARAMETER_REQUIRED = "The parameter %s is required";
    private static final String PARAMETER_NON_INTEGER = "The parameter %s should be an integer";
    private static final String PARAMETER_OUT_OF_RANGE = "The parameter %s should be the range %d...%d";
    private static final String PATH_BODY_CONFLICT = "Id %d in path differs from id %d in body";

    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 50;


    @Autowired
    EventService eventService;

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/")
    public Response createEvent(final Event event, @Context UriInfo uriInfo) {
        try {
            eventService.createEvent(event);
            URI newLocation = uriInfo.getAbsolutePathBuilder().path(event.getId() + "").build();
            return Response.created(newLocation).build();
        } catch (EventExistsException e) {
            throw new ServiceException(CONFLICT, EVENT_ALREADY_EXISTS, event.getId());
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response updateEvent(@PathParam("id") String idString, Event event) {
        int id = toInteger("id", idString);
        checkIdAgreement(id, event.getId());

        try {
            eventService.updateEvent(event);
            return Response.noContent().build();
        } catch (NoSuchEventException e) {
            throw new ServiceException(NOT_FOUND, EVENT_NOT_FOUND, id);
        }
    }

    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Event findEventById(@PathParam("id") String idString) {
        checkRequiredParameter("id", idString);
        int id = toInteger("id", idString);

        try {
            return eventService.findEventById(id);
        } catch (NoSuchEventException e) {
            throw new ServiceException(NOT_FOUND, EVENT_NOT_FOUND, id);
        }
    }

    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/")
    public List<Event> findEventsByName(@QueryParam("name") String name, @QueryParam("page") String pageString,
            @QueryParam("pageSize") String pageSizeString) {
        int page = toInteger("page", pageString);
        int pageSize = toInteger("pageSize", pageSizeString);
        checkRange("pageSize", pageSize, MIN_PAGE_SIZE, MAX_PAGE_SIZE);

        return eventService.findEventsByName(name, page, pageSize);
    }

    @DELETE
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response deleteEvent(@PathParam("id") String idString) {
        int id = toInteger("id", idString);

        try {
            Event e = eventService.findEventById(id);
            eventService.deleteEvent(e);
            return Response.noContent().build();
        } catch (NoSuchEventException ex) {
            throw new ServiceException(NOT_FOUND, EVENT_NOT_FOUND, id);
        }
    }

    private void checkRequiredParameter(String name, String value) {
        if (value == null) {
            throw new ServiceException(BAD_REQUEST, PARAMETER_REQUIRED, name);
        }

    }

    private int toInteger(String name, String value) {
        checkRequiredParameter(name, value);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ServiceException(BAD_REQUEST, PARAMETER_NON_INTEGER, name);
        }

    }

    private void checkRange(String name, int value, int low, int high) {
        if (value < low || value > high) {
            throw new ServiceException(FORBIDDEN, PARAMETER_OUT_OF_RANGE, name, low, high);
        }
    }

    private void checkIdAgreement(int idInPath, int idInBody) {
        if (idInPath != idInBody) {
            throw new ServiceException(CONFLICT, PATH_BODY_CONFLICT, idInPath, idInBody);
        }
    }
}
