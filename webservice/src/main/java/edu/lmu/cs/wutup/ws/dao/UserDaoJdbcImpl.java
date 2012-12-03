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

import edu.lmu.cs.wutup.ws.exception.NoSuchUserException;
import edu.lmu.cs.wutup.ws.exception.UserExistsException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;

@Repository
public class UserDaoJdbcImpl implements UserDao {

    private static final String CREATE_SQL = "insert into user(id, firstName, lastName, email, nickname, sessionId) values(?, ?, ?, ?, ?, ?);";
    private static final String CREATE_WITH_AUTO_GENERATE_ID = "insert into user(firstName, lastName, email, nickname, sessionId) values(?, ?, ?, ?, ?);";
    private static final String UPDATE_SQL = "update user set firstName=ifnull(?, firstName), lastName=ifnull(?, lastName), "
            + "email=ifnull(?, email), nickname=ifnull(?, nickname), sessionId=ifnull(?, sessionId) where id=?;";
    private static final String DELETE_SQL = "delete from user where id=?;";
    private static final String FIND_BY_ID_SQL = "select * from user where id=?;";
    private static final String FIND_BY_SESSION_ID_SQL = "select * from user where sessionId=?;";
    private static final String COUNT_SQL = "select count(*) from user;";
    private static final String FIND_ALL_USER_COMMENTS = "select * from (select v.*, u.firstname, u.lastname, u.email, u.nickname "
            + "from venue_comment v join user u on (authorId = u.id) union select o.*, u.firstname, u.lastname, u.email, "
            + "u.nickname from occurrence_comment o join user u on (authorId = u.id) union select e.*, u.firstname, u.lastname, "
            + "u.email, u.nickname from event_comment e join user u on (authorId = u.id)) where authorId = ? order by timestamp desc limit ? offset ?";

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
                        u.getNickname(), u.getSessionId());
            }
        } catch (DuplicateKeyException ex) {
            throw new UserExistsException();
        }
    }

    @Override
    public User findUserById(int id) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, new Object[]{id}, userRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoSuchUserException();
        }
    }

    @Override
    public User findUserBySessionId(String sessionId) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_SESSION_ID_SQL, new Object[]{sessionId}, userRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoSuchUserException();
        }
    }

    @Override
    public void updateUser(User u) {
        int rowsUpdated = jdbcTemplate.update(UPDATE_SQL, u.getFirstName(), u.getLastName(), u.getEmail(),
                u.getNickname(), u.getSessionId(), u.getId());

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
    public List<Comment> findCommentsByUserId(int id, PaginationData pagination) {
        return CommentDaoUtils.findCommentableObjectComments(jdbcTemplate, FIND_ALL_USER_COMMENTS, new Object[]{id,
                pagination.pageSize, pagination.pageNumber});
    }

    private void createUserWithGeneratedId(User u, KeyHolder keyHolder) {
        final String firstName = u.getFirstName();
        final String lastName = u.getLastName();
        final String email = u.getEmail();
        final String nickname = u.getNickname();
        final String sessionId = u.getSessionId();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(CREATE_WITH_AUTO_GENERATE_ID, new String[]{"id"});
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, nickname);
                ps.setString(5, sessionId);
                return ps;
            }
        }, keyHolder);
    }

    private static RowMapper<User> userRowMapper = new RowMapper<User>() {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"),
                    rs.getString("email"), rs.getString("nickname"), rs.getString("sessionId"));
        }
    };
}
