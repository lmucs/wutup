package edu.lmu.cs.wutup.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import edu.lmu.cs.wutup.ws.exception.CommentExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchCommentException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.User;

public class CommentDaoUtils {

    public static void addComment(JdbcTemplate jdbcTemplate, String objectName, Integer objectId, Comment comment) {
        int rowsUpdated = jdbcTemplate.update("insert into " + objectName
                + "_comment (subjectId, authorid, text, timestamp) values (?,?,?,?)", objectId, comment.getAuthor()
                .getId(), comment.getBody(), comment.getTimestamp());
        if (rowsUpdated == 0) {
            throw new CommentExistsException();
        }

    }

    public static void updateComment(JdbcTemplate jdbcTemplate, String objectName, int commentId, Comment c) {
        int rowsUpdated = jdbcTemplate.update("update " + objectName + "_comment set text=?, timestamp=? where id=?",
                c.getBody(), c.getTimestamp(), c.getId());
        if (rowsUpdated == 0) {
            throw new NoSuchCommentException();
        }
    }

    public static void deleteComment(JdbcTemplate jdbcTemplate, String objectName, int subjectId, int commentId) {
        int rowsUpdated = jdbcTemplate.update("delete from " + objectName + "_comment where subjectId=? and id=?", subjectId,
                commentId);
        if (rowsUpdated == 0) {
            throw new NoSuchCommentException();
        }
    }

    public static List<Comment> findCommentableObjectComments(JdbcTemplate jdbcTemplate, String SQL_STRING) {
        return jdbcTemplate.query(SQL_STRING, commentRowMapper);
    }

    public static List<Comment> findCommentableObjectComments(JdbcTemplate jdbcTemplate, String SQL_STRING,
            int objectId, int pageNumber, int pageSize) {
        return jdbcTemplate.query(SQL_STRING, new Object[]{objectId, pageSize, pageNumber * pageSize}, commentRowMapper);
    }

    public static RowMapper<Comment> commentRowMapper = new RowMapper<Comment>() {
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            int commentId = rs.getInt("id");
            String text = rs.getString("text");
            Timestamp persistedTimestamp = rs.getTimestamp("timestamp");
            DateTime timestamp = persistedTimestamp == null ? null : new DateTime(persistedTimestamp);
            return new Comment(commentId, text, timestamp, new User(rs.getInt("authorid"), rs.getString("firstName"),
                    rs.getString("lastName"), rs.getString("email"), rs.getString("nickname")));
        }
    };
}
