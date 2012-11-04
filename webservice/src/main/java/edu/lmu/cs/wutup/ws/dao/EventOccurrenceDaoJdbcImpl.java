package edu.lmu.cs.wutup.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import edu.lmu.cs.wutup.ws.exception.EventOccurrenceExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventOccurrenceException;
import edu.lmu.cs.wutup.ws.model.Category;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.model.Venue;
import edu.lmu.cs.wutup.ws.service.VenueService;

@Repository
public class EventOccurrenceDaoJdbcImpl implements EventOccurrenceDao {

    private static final String SELECT = "select o.id, v.name, o.venueId, o.eventId from occurrence o join venue v on (o.venueId=v.id)";
    private static final String PAGINATION = "limit ? offset ?";

    private static final String CREATE_SQL = "insert into occurrence (id, venue) values (?,?)";
    private static final String UPDATE_SQL = "update occurrence set venue=? where id=?";
    private static final String FIND_BY_ID_SQL = SELECT + " where o.id=?";
    private static final String FIND_ALL_SQL = SELECT + " " + PAGINATION;
    private static final String DELETE_SQL = "delete from occurrence where id=?";

    private static final String SELECT_COMMENT = "select ec.*, u.* from eventoccurence_comment ec join user u on (ec.authorId = u.id)";
    private static final String FIND_COMMENTS_SQL = SELECT_COMMENT + " where ec.eventId = ? " + PAGINATION;

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    VenueService venueService;

    @Override
    public void createEventOccurrence(EventOccurrence e) {
        try {
            jdbcTemplate.update(CREATE_SQL, e.getId(), e.getVenue());
        } catch (DuplicateKeyException ex) {
            throw new EventOccurrenceExistsException();
        }
    }

    @Override
    public void updateEventOccurrence(EventOccurrence e) {
        int rowsUpdated = jdbcTemplate.update(UPDATE_SQL, e.getVenue(), e.getId());
        if (rowsUpdated == 0) {
            throw new NoSuchEventOccurrenceException();
        }
    }

    @Override
    public void deleteEventOccurrence(int id) {
        int rowsUpdated = jdbcTemplate.update(DELETE_SQL, id);
        if (rowsUpdated == 0) {
            throw new NoSuchEventOccurrenceException();
        }
    }

    @Override
    public List<User> findAttendeesByEventOccurrenceId(int id, int pageNumber, int pageSize) {
        return new java.util.ArrayList<User>();
        // TODO
    }

    @Override
    public EventOccurrence findEventOccurrenceById(int id) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, new Object[]{id}, eventOccurrenceRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoSuchEventOccurrenceException();
        }
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrences(PaginationData pagination) {
        return jdbcTemplate.query(FIND_ALL_SQL,
                new Object[]{pagination.pageSize, pagination.pageNumber * pagination.pageSize},
                eventOccurrenceRowMapper);
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByAttendees(List<User> attendees, int pageNumber, int pageSize) {
        return new java.util.ArrayList<EventOccurrence>();
        // TODO
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByCategories(List<Category> categories, int pageNumber,
            int pageSize) {
        return new java.util.ArrayList<EventOccurrence>();
        // TODO
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByCenterAndRadius(double latitude, double longitude,
            double radius, int pageNumber, int pageSize) {
        return new java.util.ArrayList<EventOccurrence>();
        // TODO
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByDateRange(DateTime start, DateTime end, int pageNumber,
            int pageSize) {
        return new java.util.ArrayList<EventOccurrence>();
        // TODO
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByEventId(int eventId, int pageNumber, int pageSize) {
        return new java.util.ArrayList<EventOccurrence>();
        // TODO
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByVenues(List<Venue> venues, int pageNumber, int pageSize) {
        return new java.util.ArrayList<EventOccurrence>();
        // TODO
    }

    @Override
    public void registerAttendeeForEventOccurrence(int eventOccurrenceId, int attendeeId) {
        // TODO
    }

    @Override
    public void unregisterAttendeeForEventOccurrence(int eventOccurrenceId, int attendeeId) {
        // TODO
    }

    /* Begins the Comment Methods */
    @Override
    public void addComment(Integer eventId, Comment comment) {
        CommentDaoUtils.addComment(jdbcTemplate, "event", eventId, comment);
    }

    @Override
    public void updateComment(Integer commentId, Comment c) {
        CommentDaoUtils.updateComment(jdbcTemplate, "event", commentId, c);
    }

    @Override
    public void deleteComment(int eventId, int commentId) {
        CommentDaoUtils.deleteComment(jdbcTemplate, "event", eventId, commentId);
    }

    @Override
    public List<Comment> findComments(int eventId, PaginationData pagination) {
        return CommentDaoUtils.findCommentableObjectComments(jdbcTemplate, FIND_COMMENTS_SQL, eventId,
                pagination.pageNumber, pagination.pageSize);
    }

    private RowMapper<EventOccurrence> eventOccurrenceRowMapper = new RowMapper<EventOccurrence>() {
        public EventOccurrence mapRow(ResultSet rs, int rowNum) throws SQLException {
            System.out.println("********* Column 1: " + rs.getInt("id"));
            System.out.println("********* Column 2: " + rs.getString("name"));
            System.out.println("********* Column 3: " + rs.getInt("venueId"));
            return new EventOccurrence(rs.getInt("id"), rs.getInt("eventId"), venueService.findVenueById(rs.getInt("venueId")));
        }
    };

}
