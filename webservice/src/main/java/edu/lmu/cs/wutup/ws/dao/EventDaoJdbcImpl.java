package edu.lmu.cs.wutup.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import edu.lmu.cs.wutup.ws.exception.EventExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.User;

@Repository
public class EventDaoJdbcImpl implements EventDao {

    private static final String SELECT_EVENT = "select e.*, u.* from event e join user u on (e.ownerId = u.id)";
    private static final String SELECT_COMMENT = "select ec.*, u.* from event_comment ec join user u on (ec.authorId = u.id)";
    private static final String PAGINATION = "limit ? offset ?";

    private static final String CREATE_SQL = "insert into event (name, description, ownerId) values (?,?,?)";
    private static final String UPDATE_SQL = "update event set name=?, description=?, ownerId=? where id=?";
    private static final String FIND_BY_ID_SQL = SELECT_EVENT + " where e.id=?";
    private static final String FIND_ALL_SQL = SELECT_EVENT + " " + PAGINATION;
    private static final String FIND_BY_NAME_SQL = SELECT_EVENT + " where e.name=? " + PAGINATION;
    private static final String DELETE_SQL = "delete from event where id=?";
    private static final String COUNT_SQL = "select count(*) from event";

    private static final String CREATE_COMMENT_SQL = "insert into event_comments (eventid, authorid, text, timestamp) values (?,?,?,?)";
    private static final String UPDATE_COMMENT_SQL = "update event_comments set text=?, timestamp=? where id=?";
    private static final String DELETE_COMMENT_SQL = "delete from event_comments where eventId=? and id=?";
    private static final String FIND_COMMENTS_SQL = SELECT_COMMENT + " where ec.eventId = ? " + PAGINATION;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void createEvent(Event e) {
        try {
            jdbcTemplate.update(CREATE_SQL, e.getName(), e.getDescription(), e.getCreator().getId());
        } catch (DuplicateKeyException ex) {
            throw new EventExistsException();
        }
    }

    @Override
    public void updateEvent(Event e) {
        int rowsUpdated = jdbcTemplate.update(UPDATE_SQL, e.getName(), e.getDescription(), e.getCreator().getId(),
                e.getId());
        if (rowsUpdated == 0) {
            throw new NoSuchEventException();
        }
    }

    @Override
    public Event findEventById(int id) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, new Object[]{id}, eventRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoSuchEventException();
        }
    }

    @Override
    public List<Event> findEventsByName(String name, int pageNumber, int pageSize) {
        return jdbcTemplate.query(FIND_BY_NAME_SQL, new Object[]{name, pageSize, pageNumber * pageSize}, eventRowMapper);
    }

    @Override
    public List<Event> findAllEvents(int pageNumber, int pageSize) {
        return jdbcTemplate.query(FIND_ALL_SQL, new Object[]{pageSize, pageNumber * pageSize}, eventRowMapper);
    }

    @Override
    public void deleteEvent(int id) {
        int rowsUpdated = jdbcTemplate.update(DELETE_SQL, id);
        if (rowsUpdated == 0) {
            throw new NoSuchEventException();
        }
    }

    @Override
    public int findNumberOfEvents() {
        return jdbcTemplate.queryForInt(COUNT_SQL);
    }

    @Override
    public void addComment(Integer eventId, Comment comment) {
        CommentableDaoHelper.addComment(jdbcTemplate, CREATE_COMMENT_SQL, eventId, comment);
    }

    @Override
    public void updateComment(Integer commentId, Comment c) {
        CommentableDaoHelper.updateComment(jdbcTemplate, UPDATE_COMMENT_SQL, commentId, c);
    }

    @Override
    public void deleteComment(int eventId, int commentId) {
        CommentableDaoHelper.deleteComment(jdbcTemplate, DELETE_COMMENT_SQL, eventId, commentId);
    }

    @Override
    public List<Comment> findEventComments(int eventId, int pageNumber, int pageSize) {
        return CommentableDaoHelper.findCommentableObjectComments(jdbcTemplate, FIND_COMMENTS_SQL, eventId, pageNumber,
                pageSize);
    }

    private static RowMapper<Event> eventRowMapper = new RowMapper<Event>() {
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Event(rs.getInt("id"), rs.getString("name"), rs.getString("description"), new User(
                    rs.getInt("ownerid"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"),
                    rs.getString("nickname")));
        }
    };

}
