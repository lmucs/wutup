package edu.lmu.cs.wutup.ws.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import edu.lmu.cs.wutup.ws.exception.NoSuchVenueException;
import edu.lmu.cs.wutup.ws.exception.VenueExistsException;
import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.Venue;

@Repository
public class VenueDaoJdbcImpl implements VenueDao {

    private static final String SELECT_VENUE = "select v.* from venue v ";
    private static final String SELECT_COMMENT = "select vc.*, u.* from venue_comment vc join user u on (vc.authorId = u.id)";
    private static final String PAGINATION = "limit ? offset ?";

    private static final String FIND_COMMENTS_SQL = SELECT_COMMENT + " where vc.subjectId = ? order by vc.timestamp "
            + PAGINATION;
    private static final String CREATE_SQL = "insert into venue (id, name, address, latitude, longitude) values (?,?,?,?,?)";
    private static final String CREATE_WITH_AUTO_GENERATE_ID = "insert into venue (name, address, latitude, longitude) values (?,?,?,?)";
    private static final String UPDATE_SQL = "update venue set name=ifnull(?, name), address=ifnull(?, address), "
            + "latitude=ifnull(?, latitude), longitude=ifnull(?, longitude) where id=?";
    private static final String FIND_BY_ID_SQL = SELECT_VENUE + " where v.id=?";
    private static final String FIND_ALL_SQL = SELECT_VENUE + " " + PAGINATION;
    private static final String FIND_BY_ADDRESS_SQL = SELECT_VENUE + " where v.address=? " + PAGINATION;
    private static final String DELETE_SQL = "delete from venue where id=?";
    private static final String COUNT_SQL = "select count(*) from venue";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void createVenue(Venue v) {
        try {
            if (v.getId() == null) {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                createVenueWithGeneratedId(v, keyHolder);
                v.setId(keyHolder.getKey().intValue());
            } else {
                jdbcTemplate.update(CREATE_SQL, v.getId(), v.getName(), v.getAddress(), v.getLatitude(),
                        v.getLongitude());
            }
        } catch (DuplicateKeyException ex) {
            throw new VenueExistsException();
        }
    }

    @Override
    public void updateVenue(Venue loc) {
        int rowsUpdated = jdbcTemplate.update(UPDATE_SQL, loc.getName(), loc.getAddress(), loc.getLatitude(),
                loc.getLongitude(), loc.getId());
        if (rowsUpdated == 0) {
            throw new NoSuchVenueException();
        }
    }

    @Override
    public void deleteVenue(int venueId) {
        int rowsUpdated = jdbcTemplate.update(DELETE_SQL, venueId);
        if (rowsUpdated == 0) {
            throw new NoSuchVenueException();
        }
    }

    @Override
    public Venue findVenueById(int id) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, new Object[]{id}, venueRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoSuchVenueException();
        }
    }

    @Override
    public List<Venue> findVenues(String name, Integer eventId, Circle circle, PaginationData pagination) {
        QueryBuilder builder = new QueryBuilder().select("v.*").from("venue v");

        if (pagination != null) {
            builder.addPagination(pagination);
        }

        if (name != null) {
            builder.like("lower(v.name) like lower(:venueName)", name);
        }

        if (eventId != null) {
            builder.joinOn("occurrence o", "o.venueId = v.id");
            builder.where("o.eventId = :eventIdentifier", eventId);
        }

        if (circle != null) {
            builder.where(createCircleSearchClause(circle), circle.radius);
        }

        return jdbcTemplate.query(builder.build(), venueRowMapper);
    }

    @Override
    public int findNumberOfVenues() {
        return jdbcTemplate.queryForInt(COUNT_SQL);
    }

    @Override
    public Integer addComment(Integer venueId, Comment comment) {
        return CommentDaoUtils.addComment(jdbcTemplate, "venue", venueId, comment);
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
    public List<Comment> findComments(int venueId, PaginationData pagination) {
        return CommentDaoUtils.findCommentableObjectComments(jdbcTemplate, FIND_COMMENTS_SQL, venueId,
                pagination.pageNumber, pagination.pageSize);
    }

    @Override
    public int findMaxKeyValueForComments() {
        return CommentDaoUtils.findMaxKeyValueForComments(jdbcTemplate, "venue");
    }

    private void createVenueWithGeneratedId(Venue v, KeyHolder keyHolder) {
        final String name = v.getName();
        final String address = v.getAddress();
        final Double latitude = v.getLatitude();
        final Double longitude = v.getLongitude();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(CREATE_WITH_AUTO_GENERATE_ID, new String[]{"id"});
                ps.setString(1, name);
                ps.setString(2, address);
                ps.setDouble(3, latitude);
                ps.setDouble(4, longitude);
                return ps;
            }
        }, keyHolder);
    }

    @Override
    public Map<String, String> findProperties(int venueId) {
        QueryBuilder builder = new QueryBuilder().select("*").from("venue_property").where("venueId = :id", venueId);
        List<String[]> keysToValues = jdbcTemplate.query(builder.build(), propertyRowMapper);
        Map<String, String> properties = new HashMap<String, String>();
        for (int i = 0; i < keysToValues.size(); i++) {
            properties.put(keysToValues.get(i)[0], keysToValues.get(i)[1]);
        }
        return properties;
    }

    @Override
    public void addProperty(String propertyName, String value) {
        // TODO Auto-generated method stub
    }

    private static String createCircleSearchClause(Circle c) {
        return "get_distance_miles(v.latitude, " + c.centerLatitude + ", v.longitude, " + c.centerLongitude
                + ") <= :radius";
    }

    private static RowMapper<Venue> venueRowMapper = new RowMapper<Venue>() {
        public Venue mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Venue(rs.getInt("id"), rs.getString("name"), rs.getString("address"), rs.getDouble("latitude"),
                    rs.getDouble("longitude"), null);
        }
    };

    private static RowMapper<String[]> propertyRowMapper = new RowMapper<String[]>() {
        public String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new String[]{rs.getString("key"), rs.getString("value")};
        }
    };

}
