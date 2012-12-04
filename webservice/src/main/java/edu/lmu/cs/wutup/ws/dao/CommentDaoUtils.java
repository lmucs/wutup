package edu.lmu.cs.wutup.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import edu.lmu.cs.wutup.ws.exception.NoSuchCommentException;
import edu.lmu.cs.wutup.ws.exception.NoSuchResourceException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.User;

public class CommentDaoUtils {

    private static User knownAuthor = null;

    public static Integer addComment(JdbcTemplate jdbcTemplate, String objectName, Integer objectId, Comment comment) {
        String create_sql = "insert into " + objectName
                + "_comment(subjectId, authorId, text, timestamp) values(?,?,?,?)";
        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(create_sql, new int[]{
                Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.TIMESTAMP});
        factory.setReturnGeneratedKeys(true);
        factory.setGeneratedKeysColumnNames(new String[]{"id"});
        PreparedStatementCreator creator = factory.newPreparedStatementCreator(new Object[]{objectId,
                comment.getAuthor().getId(), comment.getBody(), new Timestamp(comment.getPostDate().getMillis())});
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(creator, keyHolder);
            return (Integer) keyHolder.getKey();
        } catch (DataIntegrityViolationException e) {
            throw new NoSuchResourceException();
        }

    }

    public static void updateComment(JdbcTemplate jdbcTemplate, String objectName, int commentId, Comment c) {
        int rowsUpdated = jdbcTemplate.update("update " + objectName + "_comment set text=?, timestamp=? where id=?",
                c.getBody(), new Timestamp(c.getPostDate().getMillis()), c.getId());
        if (rowsUpdated == 0) {
            throw new NoSuchCommentException();
        }
    }

    public static void deleteComment(JdbcTemplate jdbcTemplate, String objectName, int subjectId, int commentId) {
        int rowsUpdated = jdbcTemplate.update("delete from " + objectName + "_comment where subjectId=? and id=?",
                subjectId, commentId);
        if (rowsUpdated == 0) {
            throw new NoSuchCommentException();
        }
    }

    public static List<Comment> findCommentableObjectComments(JdbcTemplate jdbcTemplate, String SQL_STRING, User author) {
        knownAuthor = author;
        return jdbcTemplate.query(SQL_STRING, commentRowMapper);
    }

    public static List<Comment> findCommentableObjectComments(JdbcTemplate jdbcTemplate, String SQL_STRING,
            Object[] args, User author) {
        knownAuthor = author;
        return jdbcTemplate.query(SQL_STRING, args, commentRowMapper);
    }

    public static int findMaxKeyValueForComments(JdbcTemplate jdbcTemplate, String objectName) {
        return jdbcTemplate.queryForInt("select max(id) from " + objectName + "_comment");
    }

    public static RowMapper<Comment> commentRowMapper = new RowMapper<Comment>() {
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            int commentId = rs.getInt("id");
            String text = rs.getString("text");
            Timestamp persistedTimestamp = rs.getTimestamp("timestamp");
            DateTime timestamp = persistedTimestamp == null ? null : new DateTime(persistedTimestamp);
            User author = knownAuthor == null ? new User(rs.getInt("authorid"), rs.getString("firstName"),
                    rs.getString("lastName"), rs.getString("email"), rs.getString("nickname"),
                    rs.getString("facebookId")) : knownAuthor;
            return new Comment(commentId, text, timestamp, author);
        }
    };
}
