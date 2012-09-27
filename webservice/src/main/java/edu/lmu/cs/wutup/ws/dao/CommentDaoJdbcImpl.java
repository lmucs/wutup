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

import edu.lmu.cs.wutup.ws.exception.CommentExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchCommentException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.User;

@Repository
public class CommentDaoJdbcImpl implements CommentDao {

    private static final String CREATE_SQL = "insert into Comment (id, Body, timestamp) values (?,?,?)";
    private static final String UPDATE_SQL = "update Comment set Body=? where id=?";
    private static final String FIND_BY_ID_SQL = "select id, Body from Comment where id=?";
    private static final String FIND_BY_USER_SQL = "select id, Body from Comment where user=?";
    private static final String DELETE_SQL = "delete from Comment where id=?";
    private static final String COUNT_SQL = "select count(*) from Comment";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void createComment(Comment e) {
        try {
            jdbcTemplate.update(CREATE_SQL, e.getId(), e.getBody());
        } catch (DuplicateKeyException ex) {
            throw new CommentExistsException();
        }
    }

    @Override
    public void updateComment(Comment e) {
        int rowsUpdated = jdbcTemplate.update(UPDATE_SQL, e.getBody(), e.getId());
        if (rowsUpdated == 0) {
            throw new NoSuchCommentException();
        }
    }

    @Override
    public Comment findCommentById(int id) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, new Object[]{id}, CommentRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoSuchCommentException();
        }
    }

    @Override
    public List<Comment> findCommentsByUserId(int user) {
        return jdbcTemplate.query(FIND_BY_USER_SQL, new Object[]{user},
                CommentRowMapper);
    }

    @Override
    public void deleteComment(Comment e) {
        int rowsUpdated = jdbcTemplate.update(DELETE_SQL, e.getId());
        if (rowsUpdated == 0) {
            throw new NoSuchCommentException();
        }
    }

    @Override
    public int findNumberOfComments() {
        return jdbcTemplate.queryForInt(COUNT_SQL);
    }

    private static RowMapper<Comment> CommentRowMapper = new RowMapper<Comment>() {
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Comment(rs.getString("body"), (User)rs.getObject("user"), (DateTime)rs.getObject("timestamp"));
        }
    };
}
