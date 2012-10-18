package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Venue;

@Repository
public class VenueDaoJdbcImpl implements VenueDao {

    private static final String SELECT_COMMENT = "select ec.*, u.* from venue_comment ec join user u on (ec.authorId = u.id)";
    private static final String PAGINATION = "limit ? offset ?";

    private static final String FIND_COMMENTS_SQL = SELECT_COMMENT + " where ec.venueId = ? " + PAGINATION;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void createVenue(Venue loc) {
        // TODO Auto-generated method stub

    }

    @Override
    public Venue findVenueById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Venue> findVenuesByAddress(String address, int pageNumber, int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Venue> findVenuesByPropertyMap(String propertyMap, int pageNumber, int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Venue> findAllVenues(int pageNumber, int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateVenue(Venue loc) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteVenue(Venue loc) {
        // TODO Auto-generated method stub

    }

    @Override
    public int findNumberOfVenues() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void addComment(Integer venueId, Comment comment) {
        CommentDaoUtils.addComment(jdbcTemplate, "venue", venueId, comment);
    }

    @Override
    public void updateComment(Integer commentId, Comment c) {
        CommentDaoUtils.updateComment(jdbcTemplate, "venue", commentId, c);
    }

    @Override
    public void deleteComment(int venueId, int commentId) {
        CommentDaoUtils.deleteComment(jdbcTemplate, "venue", venueId, commentId);
    }

    @Override
    public List<Comment> findComments(int venueId, int pageNumber, int pageSize) {
        return CommentDaoUtils.findCommentableObjectComments(jdbcTemplate, FIND_COMMENTS_SQL, venueId, pageNumber,
                pageSize);
    }

}
