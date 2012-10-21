package edu.lmu.cs.wutup.ws.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

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

import edu.lmu.cs.wutup.ws.model.User;

@Repository
public class UserDaoJdbcImpl implements UserDao {
    
    private static final String CREATE_SQL = "insert into user(id, firstName, lastName, email, nickname) values(?, ?, ?, ?, ?);";
    private static final String CREATE_WITH_AUTO_GENERATE_ID = "insert into user(firstName, lastName, email, nickname) values(?, ?, ?, ?);";
    private static final String UPDATE_SQL = "update user set firstName=?, lastName=?, email=?, nickname=? where id=?;";
    private static final String DELETE_SQL = "delete from user where id=?;";
    private static final String FIND_BY_ID_SQL = "select * from user where id=?;";
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
                jdbcTemplate.update(CREATE_SQL, u.getId(), u.getFirstName(), 
                        u.getLastName(), u.getEmail(), u.getNickname());
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
    public void updateUser(User u) {
        int rowsUpdated = jdbcTemplate.update(
                UPDATE_SQL, u.getFirstName(), u.getLastName(), u.getEmail(), u.getNickname(), u.getId());
        
        if (rowsUpdated == 0) {
            throw new NoSuchUserException();
        }
    }
    
    @Override
    public int findNumberOfUsers() {
        return jdbcTemplate.queryForInt(COUNT_SQL);
    }
    
    @Override
    public void deleteUser(User u) {
        int rowsUpdated = jdbcTemplate.update(DELETE_SQL, u.getId());
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
    
    private void createUserWithGeneratedId(User u, KeyHolder keyHolder) {
        final String firstName = u.getFirstName();
        final String lastName = u.getLastName();
        final String email = u.getEmail();
        final String nickname = u.getNickname();
        jdbcTemplate.update(
            new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps =
                        connection.prepareStatement(CREATE_WITH_AUTO_GENERATE_ID, new String[] {"id"});
                    ps.setString(1, firstName);
                    ps.setString(2, lastName);
                    ps.setString(3, email);
                    ps.setString(4, nickname);
                    return ps;
                }
            },
            keyHolder);
    }
    
    private static RowMapper<User> userRowMapper = new RowMapper<User>() {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getInt("id"), 
                    rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("nickname"));
        }
    };
    
}
