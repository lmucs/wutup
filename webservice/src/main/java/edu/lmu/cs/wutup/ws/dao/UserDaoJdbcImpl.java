package edu.lmu.cs.wutup.ws.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import edu.lmu.cs.wutup.ws.model.User;

public class UserDaoJdbcImpl implements UserDao {
    
    private static final String CREATE_SQL = "insert into user(id, firstName, lastName, email, nickname) values(?, ?, ?, ?, ?);";
    private static final String UPDATE_SQL = "update user set email=? where id=?;";
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void createUser(User u) {
        try {
            jdbcTemplate.update(CREATE_SQL, u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getNickname());
        } catch(DuplicateKeyException e) {
            // TODO Go over Exceptions with Dr.Toal
        }
    }

    @Override
    public User findUserById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateUser(User u) {
        // TODO Auto-generated method stub
        
    }
}
