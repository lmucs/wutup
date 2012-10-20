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
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.model.Venue;

@Repository
public class EventOccurrenceDaoJdbcImpl implements EventOccurrenceDao {

    private static final String CREATE_SQL = "insert into eventOccurrence (id, venue) values (?,?)";
    private static final String UPDATE_SQL = "update eventOccurrence set venue=? where id=?";
    private static final String FIND_BY_ID_SQL = "select id, name from eventOccurrence where id=?";
    private static final String FIND_ALL_SQL = "select id, name from eventOccurrence limit ? offset ?";
    private static final String DELETE_SQL = "delete from eventOccurrence where id=?";
    private static final String COUNT_SQL = "select count(*) from eventOccurrence";

    private static final String SELECT_COMMENT = "select ec.*, u.* from eventoccurence_comment ec join user u on (ec.authorId = u.id)";
    private static final String PAGINATION = "limit ? offset ?";
    private static final String FIND_COMMENTS_SQL = SELECT_COMMENT + " where ec.eventId = ? " + PAGINATION;

    @Autowired
    JdbcTemplate jdbcTemplate;

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
    public List<User> findAttendeesById(int id, int pageNumber, int pageSize) {
        return new java.util.ArrayList<User>();
        // TODO
    }

    @Override
    public int findNumberOfEventOccurrences() {
        return jdbcTemplate.queryForInt(COUNT_SQL);
    }

    @Override
    public EventOccurrence findEventOccurrenceById(int id) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, new Object[]{id}, eventOccurrenceRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoSuchEventOccurrenceException();
        }
    }

    /*
     * OLD: @Override public List<EventOccurrence> findAllEventOccurrences(int pageNumber, int pageSize) { return
     * jdbcTemplate.query(FIND_ALL_SQL, new Object[]{pageSize, pageNumber * pageSize}, eventOccurrenceRowMapper); }
     */

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
    public void registerAttendeeForEventOccurrence(User attendee) {
        // TODO
    }

    @Override
    public void unregisterAttendeeForEventOccurrence(User attendee) {
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
    public List<Comment> findComments(int eventId, int pageNumber, int pageSize) {
        return CommentDaoUtils.findCommentableObjectComments(jdbcTemplate, FIND_COMMENTS_SQL, eventId, pageNumber,
                pageSize);
    }

    private static RowMapper<EventOccurrence> eventOccurrenceRowMapper = new RowMapper<EventOccurrence>() {
        public EventOccurrence mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new EventOccurrence(rs.getInt("id"), VenueParser(rs.getString("venue")));
        }
    };

    private static Venue VenueParser(String venue) {
        String[] venueAttributes = venue.split(",");
        // TODO: Ask Dr. Toal if this is good enough
        try {
            return new Venue(Integer.parseInt(venueAttributes[0]), venueAttributes[1], venueAttributes[2],
                    Double.parseDouble(venueAttributes[3]), Double.parseDouble(venueAttributes[4]), null);
        } catch (Exception e) {
            // Do we have a log to write to?
            return new Venue();
        }
    }

}
