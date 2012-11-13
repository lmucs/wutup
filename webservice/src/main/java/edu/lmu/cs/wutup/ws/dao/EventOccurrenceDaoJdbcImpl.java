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
import edu.lmu.cs.wutup.ws.exception.EventOccurrenceExistsException;
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

    private static final String PAGINATION = "limit ? offset ?";
    private static final String CREATE_SQL = "insert into occurrence (eventId,venueId,start,end) values (?,?,?,?)";
    private static final String UPDATE_SQL = "update occurrence set venueid=ifnull(?, venueid), eventid=ifnull(?, eventid), "
            + "start=ifnull(?, start), end=ifnull(?, end) where id=?";
    private static final String DELETE_SQL = "delete from occurrence where id=?";
    private static final String COUNT_SQL = "select count(*) from occurrence";

    private static final String SELECT_COMMENT = "select ec.*, u.* from eventoccurence_comment ec join user u on (ec.authorId = u.id)";
    private static final String FIND_COMMENTS_SQL = SELECT_COMMENT + " where ec.eventId = ? " + PAGINATION;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int createEventOccurrence(EventOccurrence e) {
        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(CREATE_SQL, new int[]{
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
        int rowsUpdated = jdbcTemplate.update(UPDATE_SQL, e.getVenue().getId(), e.getEvent().getId(), new Timestamp(
                e.getStart().getMillis()), new Timestamp(e.getEnd().getMillis()), e.getId());
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
    public List<User> findAttendeesByEventOccurrenceId(int id, PaginationData pagination) {
        return new java.util.ArrayList<User>();
        // TODO
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
            Integer eventId, List<Venue> venues, PaginationData pagination) {
        ;
        // TODO: change this

        return jdbcTemplate.query(getSelectQuery().whereCircle(circle)
                .where("o.eventid = :eventid", eventId)
                .whereInterval(interval)
                .addPagination(pagination)
                .build(), eventOccurrenceRowMapper);
    }

    public int findNumberOfEventOccurrences() {
        return jdbcTemplate.queryForInt(COUNT_SQL);
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
    public Integer addComment(Integer eventId, Comment comment) {
        return CommentDaoUtils.addComment(jdbcTemplate, "event", eventId, comment);
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
            int occurrenceId = rs.getInt("id");
            DateTime start = new DateTime(rs.getDate("start"));
            DateTime end = new DateTime(rs.getDate("end"));
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

    private QueryBuilder getSelectQuery() {
        return new QueryBuilder().select("o.id", "o.start", "o.end", "o.venueId", "v.name as venueName", "v.address",
                "v.latitude", "v.longitude", "o.eventId", "e.name as eventName", "e.description", "address",
                "u.id as userId", "u.firstName", "u.lastName", "u.email", "u.nickname")
                .from("occurrence o")
                .joinOn("venue v", "(o.venueId = v.id)")
                .joinOn("event e", "(o.eventId = e.id)")
                .joinOn("user u", "(e.ownerId = u.id)");
    }
}
