package edu.lmu.cs.wutup.ws.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import edu.lmu.cs.wutup.ws.exception.CategoryExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchCategoryException;
import edu.lmu.cs.wutup.ws.model.Category;

public class CategoryDaoJdbcImpl implements CategoryDao {

    private static final String CREATE_SQL = "insert into category(id, name, parentId) values(?, ?, ?);";
    private static final String CREATE_WITH_AUTO_GENERATE_ID = "insert into category(name, parentId) values(?, ?);";
    private static final String UPDATE_SQL = "update category set name=?, parentId=? where id=?;";
    private static final String DELETE_SQL = "delete from category where id=?;";
    private static final String FIND_BY_ID_SQL = "select * from category where id=?;";
    private static final String COUNT_SQL = "select count(*) from category;";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void createCategory(Category c) {
        try {
            if (c.getId() == null) {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                createCategoryWithGeneratedId(c, keyHolder);
                c.setId(keyHolder.getKey().intValue());
            } else {
                jdbcTemplate.update(CREATE_SQL, c.getId(), c.getName(),
                        c.getParentId());
            }
        } catch (DuplicateKeyException ex) {
            throw new CategoryExistsException();
        }
    }

    @Override
    public Category findCategoryById(int id) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, new Object[]{id}, categoryRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoSuchCategoryException();
        }
    }

    @Override
    public void updateCategory(Category c) {
        int rowsUpdated = jdbcTemplate.update(
                UPDATE_SQL, c.getName(), c.getParentId(), c.getId());

        if (rowsUpdated == 0) {
            throw new NoSuchCategoryException();
        }
    }

    @Override
    public int findNumberOfCategories() {
        return jdbcTemplate.queryForInt(COUNT_SQL);
    }

    @Override
    public void deleteCategory(int id) {
        int rowsUpdated = jdbcTemplate.update(DELETE_SQL, id);
        if (rowsUpdated == 0) {
            throw new NoSuchCategoryException();
        }
    }

    @Override
    public int getMaxValueFromColumn(String columnName) {
        String queryForMax = "select max(" + columnName + ") from category;";
        return jdbcTemplate.queryForInt(queryForMax);
    }

    @Override
    public int getNextUsableCategoryId() {
        int largestIdValue = getMaxValueFromColumn("id");
        return largestIdValue + 1;
    }

    private void createCategoryWithGeneratedId(Category c, KeyHolder keyHolder) {
        final String name = c.getName();
        final Integer parentId = c.getParentId();
        jdbcTemplate.update(
            new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps =
                        connection.prepareStatement(CREATE_WITH_AUTO_GENERATE_ID, new String[] {"id"});
                    ps.setString(1, name);

                    if (parentId == null) {
                        ps.setNull(2,  java.sql.Types.NULL);
                    } else {
                        ps.setInt(2,  parentId);
                    }

                    return ps;
                }
            },
            keyHolder);
    }

    private static RowMapper<Category> categoryRowMapper = new RowMapper<Category>() {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Category.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .parentId(rs.getInt("parentId"))
                    .build();
        }
    };
}
