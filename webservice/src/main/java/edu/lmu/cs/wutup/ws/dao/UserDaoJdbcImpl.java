package edu.lmu.cs.wutup.ws.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import edu.lmu.cs.wutup.ws.dao.util.QueryBuilder;
import edu.lmu.cs.wutup.ws.exception.NoSuchUserException;
import edu.lmu.cs.wutup.ws.exception.UserExistsException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;

@Repository
public class UserDaoJdbcImpl implements UserDao {

    private static final String CREATE_SQL = "insert into user(id, firstName, lastName, email, nickname, sessionId, facebookId) values(?, ?, ?, ?, ?, ?, ?);";
    private static final String CREATE_WITH_AUTO_GENERATE_ID = "insert into user(firstName, lastName, email, nickname, sessionId, facebookId) values(?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_SQL = "update user set firstName=ifnull(?, firstName), lastName=ifnull(?, lastName), "
            + "email=ifnull(?, email), nickname=ifnull(?, nickname), sessionId=ifnull(?, sessionId), facebookId=ifnull(?, facebookId) where id=?;";
    private static final String DELETE_SQL = "delete from user where id=?;";
    private static final String COUNT_SQL = "select count(*) from user;";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void createUser(User u) {
        try {
            if (u.getId() == null) {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                createUserWithGeneratedId(u, keyHolder);
                u.setId(keyHolder.getKey().intValue());
            } else {
                jdbcTemplate.update(CREATE_SQL, u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(),
                        u.getNickname(), u.getSessionId(), u.getFacebookId());
            }
        } catch (DuplicateKeyException ex) {
            throw new UserExistsException();
        }
    }

    @Override
    public User findUserById(int id) {
        try {
            QueryBuilder query = getQueryOnUserWithIdFieldClause("id", id);
            return jdbcTemplate.queryForObject(query.build(), query.getParametersArray(), userRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoSuchUserException();
        }
    }

    @Override
    public User findUserBySessionId(String sessionId) {
        try {
            QueryBuilder query = getQueryOnUserWithIdFieldClause("sessionId", sessionId);
            return jdbcTemplate.queryForObject(query.build(), query.getParametersArray(), userRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoSuchUserException();
        }
    }

    @Override
    public User findUserByFacebookId(String id) {
        try {
            QueryBuilder query = getQueryOnUserWithIdFieldClause("facebookId", id);
            return jdbcTemplate.queryForObject(query.build(), query.getParametersArray(), userRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoSuchUserException();
        }
    }

    @Override
    public void updateUser(User u) {
        int rowsUpdated = jdbcTemplate.update(UPDATE_SQL, u.getFirstName(), u.getLastName(), u.getEmail(),
                u.getNickname(), u.getSessionId(), u.getFacebookId(), u.getId());

        if (rowsUpdated == 0) {
            throw new NoSuchUserException();
        }
    }

    @Override
    public int findNumberOfUsers() {
        return jdbcTemplate.queryForInt(COUNT_SQL);
    }

    @Override
    public void deleteUser(int id) {
        int rowsUpdated = jdbcTemplate.update(DELETE_SQL, id);
        if (rowsUpdated == 0) {
            throw new NoSuchUserException();
        }
    }

    @Override
    public int getMaxValueFromColumn(String columnName) {
        String queryForMax = "select max(" + columnName + ") from user;";
        return jdbcTemplate.queryForInt(queryForMax);
    }

    @Override
    public int getNextUsableUserId() {
        int largestIdValue = getMaxValueFromColumn("id");
        return largestIdValue + 1;
    }

    @Override
    public List<Comment> findCommentsByUser(User author, PaginationData pagination) {
        QueryBuilder builder = new QueryBuilder().select("*")
                .from("(select v.* from venue_comment v union select o.* from occurrence_comment o union select e.* from event_comment e)")
                .where("authorId = :userId", author.getId())
                .order("timestamp desc")
                .addPagination(pagination);
        return CommentDaoUtils.findCommentableObjectComments(jdbcTemplate, builder.build(),
                builder.getParametersArray(), author);
    }

    private QueryBuilder getQueryOnUserWithIdFieldClause(String idField, Object parameter) {
        return new QueryBuilder().from("user").where(idField + "=:" + idField, parameter);
    }

    private void createUserWithGeneratedId(User u, KeyHolder keyHolder) {
        final String firstName = u.getFirstName();
        final String lastName = u.getLastName();
        final String email = u.getEmail();
        final String nickname = u.getNickname();
        final String sessionId = u.getSessionId();
        final String facebookId = u.getFacebookId();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(CREATE_WITH_AUTO_GENERATE_ID, new String[]{"id"});
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, nickname);
                ps.setString(5, sessionId);
                ps.setString(6, facebookId);
                return ps;
            }
        }, keyHolder);
    }

    private static RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"),
                    rs.getString("email"), rs.getString("nickname"), rs.getString("sessionId"),
                    rs.getString("facebookId"));
        }
    };
}
