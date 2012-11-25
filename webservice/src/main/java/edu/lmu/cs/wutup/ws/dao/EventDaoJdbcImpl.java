package edu.lmu.cs.wutup.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

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
import edu.lmu.cs.wutup.ws.exception.EventExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventException;
import edu.lmu.cs.wutup.ws.model.Category;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;

@Repository
public class EventDaoJdbcImpl implements EventDao {

    private static final String SELECT_COMMENT = "select ec.*, u.* from event_comment ec join user u on (ec.authorId = u.id)";
    private static final String PAGINATION = "limit ? offset ?";

    private static final String CREATE_SQL = "insert into event (name, description, ownerId) values (?,?,?)";
    private static final String UPDATE_SQL = "update event set name=ifnull(?, name), description=ifnull(?, description) where id=?";
    private static final String DELETE_SQL = "delete from event where id=?";
    private static final String COUNT_SQL = "select count(*) from event";

    private static final String FIND_COMMENTS_SQL = SELECT_COMMENT
            + " where ec.subjectId = ? order by ec.timestamp asc " + PAGINATION;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int createEvent(Event e) {
        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(CREATE_SQL, new int[]{
                Types.VARCHAR, Types.VARCHAR, Types.INTEGER});
        factory.setReturnGeneratedKeys(true);
        factory.setGeneratedKeysColumnNames(new String[]{"id"});
        PreparedStatementCreator creator = factory.newPreparedStatementCreator(new Object[]{e.getName(),
                e.getDescription(), e.getCreator().getId()});
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(creator, keyHolder);
            return (Integer) keyHolder.getKey();
        } catch (DuplicateKeyException ex) {
            throw new EventExistsException();
        }
    }

    @Override
    public void updateEvent(Event e) {
        int rowsUpdated = jdbcTemplate.update(UPDATE_SQL, e.getName(), e.getDescription(), e.getId());
        if (rowsUpdated == 0) {
            throw new NoSuchEventException();
        }
    }

    @Override
    public Event findEventById(int id) {
        try {
            return jdbcTemplate.queryForObject(getSelectQuery().where("e.id=:id", id).build(), eventRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoSuchEventException();
        }
    }

    @Override
    public List<Event> findEvents(String name, List<Integer> owners, List<Category> categories, PaginationData pagination) {
        QueryBuilder query = getSelectQuery();

        if (name != null) {
            query.like("e.name", "name", name);
        }
        if (owners != null) {
            for (Integer owner : owners) {
                query.where("e.ownerId = :id", owner);
            }
        }
        if (categories != null) {
            // TODO: Implement categories search
            for (int i = 0; i < categories.size(); i++) {
                query.where(null, categories.get(i));
            }
        }

        return jdbcTemplate.query(query.addPagination(pagination).build(), eventRowMapper);
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
    public Integer addComment(Integer eventId, Comment comment) {
        return CommentDaoUtils.addComment(jdbcTemplate, "event", eventId, comment);
    }

    @Override
    public void updateComment(Integer commentId, Comment comment) {
        CommentDaoUtils.updateComment(jdbcTemplate, "event", commentId, comment);
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

    @Override
    public int findMaxKeyValueForComments() {
        return CommentDaoUtils.findMaxKeyValueForComments(jdbcTemplate, "event");
    }

    private static RowMapper<Event> eventRowMapper = new RowMapper<Event>() {
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Event(rs.getInt("id"), rs.getString("name"), rs.getString("description"), new User(
                    rs.getInt("ownerid"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"),
                    rs.getString("nickname")));
        }
    };

    private QueryBuilder getSelectQuery() {
        return new QueryBuilder().select("e.*", "u.*").from("event e").joinOn("user u", "e.ownerId = u.id");
    }
}
