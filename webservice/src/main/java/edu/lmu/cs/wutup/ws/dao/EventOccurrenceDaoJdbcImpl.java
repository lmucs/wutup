package edu.lmu.cs.wutup.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import edu.lmu.cs.wutup.ws.dao.util.QueryBuilder;
import edu.lmu.cs.wutup.ws.exception.AttendeeExistsException;
import edu.lmu.cs.wutup.ws.exception.EventOccurrenceExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchAttendeeException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventOccurrenceException;
import edu.lmu.cs.wutup.ws.model.Category;
import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.model.Venue;

@Repository
public class EventOccurrenceDaoJdbcImpl implements EventOccurrenceDao {

    private static final String CREATE_OCCURRENCE_SQL = "insert into occurrence (eventId,venueId,start,end) values (?,?,?,?)";
    private static final String UPDATE_OCCURRENCE_SQL = "update occurrence set venueid=ifnull(?, venueid), "
            + "eventid=ifnull(?, eventid), start=ifnull(?, start), end=ifnull(?, end) where id=?";
    private static final String DELETE_OCCURRENCE_SQL = "delete from occurrence where id=?";

    private static final String CREATE_ATTENDEE_SQL = "insert into attendee (occurrenceId,userId) values (?,?)";
    private static final String DELETE_ATTENDEE_SQL = "delete from attendee where occurrenceId=? and userId=?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int createEventOccurrence(EventOccurrence e) {
        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(CREATE_OCCURRENCE_SQL, new int[]{
                Types.INTEGER, Types.INTEGER, Types.TIMESTAMP, Types.TIMESTAMP});
        factory.setReturnGeneratedKeys(true);
        factory.setGeneratedKeysColumnNames(new String[]{"id"});
        PreparedStatementCreator creator = factory.newPreparedStatementCreator(new Object[]{e.getEvent().getId(),
                e.getVenue().getId(), new Timestamp(e.getStart().getMillis()), new Timestamp(e.getEnd().getMillis())});
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(creator, keyHolder);
            return (Integer) keyHolder.getKey();
        } catch (DuplicateKeyException ex) {
            throw new EventOccurrenceExistsException();
        }
    }

    @Override
    public void updateEventOccurrence(EventOccurrence e) {
        Integer venueId = e.getVenue() != null ? e.getVenue().getId() : null;
        Integer eventId = e.getEvent() != null ? e.getEvent().getId() : null;
        Timestamp start = e.getStart() != null ? new Timestamp(e.getStart().getMillis()) : null;
        Timestamp end = e.getEnd() != null ? new Timestamp(e.getEnd().getMillis()) : null;
        int rowsUpdated = jdbcTemplate.update(UPDATE_OCCURRENCE_SQL, venueId, eventId, start, end, e.getId());
        if (rowsUpdated == 0) {
            throw new NoSuchEventOccurrenceException();
        }
    }

    @Override
    public void deleteEventOccurrence(int id) {
        int rowsUpdated = jdbcTemplate.update(DELETE_OCCURRENCE_SQL, id);
        if (rowsUpdated == 0) {
            throw new NoSuchEventOccurrenceException();
        }
    }

    @Override
    public EventOccurrence findEventOccurrenceById(int id) {
        try {
            return jdbcTemplate.queryForObject(getSelectQuery().where("o.id = :id", id).build(),
                    eventOccurrenceRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoSuchEventOccurrenceException();
        }
    }

    @Override
    public List<EventOccurrence> findEventOccurrences(List<Category> categories, Circle circle, Interval interval,
            Integer eventId, Integer venueId, PaginationData pagination) {
        QueryBuilder query = getSelectQuery().whereCircle(circle)
                .where("o.eventid = :eventid", eventId)
                .where("o.venueid = :venueid", venueId)
                .whereInterval(interval);

        if (categories != null) {
            for (int i = 0; i < categories.size(); i++) {
                // TODO
                query.where(null, categories.get(i));
            }
        }

        return jdbcTemplate.query(query.addPagination(pagination).build(), eventOccurrenceRowMapper);
    }

    public int findNumberOfEventOccurrences() {
        return jdbcTemplate.queryForInt(new QueryBuilder().select("count(*)").from("occurrence").build());
    }

    @Override
    public List<User> findAttendeesByEventOccurrenceId(int id, PaginationData pagination) {
        QueryBuilder query = new QueryBuilder().from("attendee a")
                .joinOn("user u", "a.userId = u.id")
                .where("a.occurrenceId = :oId", id);
        return jdbcTemplate.query(query.addPagination(pagination).build(), userRowMapper);
    }

    @Override
    public void registerAttendeeForEventOccurrence(int eventOccurrenceId, int attendeeId) {
        try {
            jdbcTemplate.update(CREATE_ATTENDEE_SQL, eventOccurrenceId, attendeeId);
        } catch (DuplicateKeyException ex) {
            throw new AttendeeExistsException();
        }
    }

    @Override
    public void unregisterAttendeeForEventOccurrence(int eventOccurrenceId, int attendeeId) {
        int rowsUpdated = jdbcTemplate.update(DELETE_ATTENDEE_SQL, eventOccurrenceId, attendeeId);
        if (rowsUpdated == 0) {
            throw new NoSuchAttendeeException();
        }
    }

    /* Begins the Comment Methods */
    @Override
    public Integer addComment(Integer eventId, Comment comment) {
        return CommentDaoUtils.addComment(jdbcTemplate, "occurrence", eventId, comment);
    }

    @Override
    public void updateComment(Integer commentId, Comment c) {
        CommentDaoUtils.updateComment(jdbcTemplate, "occurrence", commentId, c);
    }

    @Override
    public void deleteComment(int eventId, int commentId) {
        CommentDaoUtils.deleteComment(jdbcTemplate, "occurrence", eventId, commentId);
    }

    @Override
    public List<Comment> findComments(int eventId, PaginationData pagination) {
        QueryBuilder query = new QueryBuilder().select("oc.*, u.*")
                .from("occurrence_comment oc")
                .joinOn("user u", "oc.authorId = u.id")
                .where("oc.subjectId = :subjectId", eventId)
                .order("oc.timestamp")
                .addPagination(pagination);
        return CommentDaoUtils.findCommentableObjectComments(jdbcTemplate, query.build());
    }

    @Override
    public int findMaxKeyValueForComments() {
        return CommentDaoUtils.findMaxKeyValueForComments(jdbcTemplate, "occurrence");
    }

    private RowMapper<EventOccurrence> eventOccurrenceRowMapper = new RowMapper<EventOccurrence>() {
        public EventOccurrence mapRow(ResultSet rs, int rowNum) throws SQLException {
            int occurrenceId = rs.getInt("id");
            DateTime start = new DateTime(rs.getTimestamp("start"));
            DateTime end = new DateTime(rs.getTimestamp("end"));
            int venueId = rs.getInt("venueId");
            String venueName = rs.getString("venueName");
            String address = rs.getString("address");
            Double latitude = rs.getDouble("latitude");
            Double longitude = rs.getDouble("longitude");
            int eventId = rs.getInt("eventId");
            String eventName = rs.getString("eventName");
            String description = rs.getString("description");
            int userId = rs.getInt("userId");
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            String nickname = rs.getString("nickname");
            String email = rs.getString("email");

            User user = new User(userId, firstName, lastName, nickname, email);
            Event event = new Event(eventId, eventName, description, user);
            Venue venue = new Venue(venueId, venueName, address, latitude, longitude, null);
            return new EventOccurrence(occurrenceId, event, venue, start, end);
        }
    };

    // TODO: Factor out repeated code from UserDaoJdbcImpl
    private static RowMapper<User> userRowMapper = new RowMapper<User>() {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"),
                    rs.getString("email"), rs.getString("nickname"));
        }
    };

    private QueryBuilder getSelectQuery() {
        return new QueryBuilder().select("o.id", "o.start", "o.end", "o.venueId", "v.name as venueName", "v.address",
                "v.latitude", "v.longitude", "o.eventId", "e.name as eventName", "e.description", "address",
                "u.id as userId", "u.firstName", "u.lastName", "u.email", "u.nickname")
                .from("occurrence o")
                .joinOn("venue v", "o.venueId = v.id")
                .joinOn("event e", "o.eventId = e.id")
                .joinOn("user u", "e.ownerId = u.id");
    }
}
