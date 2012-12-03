package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;

public interface UserDao {

    void createUser(User u);

    User findUserById(int id);

    User findUserBySessionId(String sessionId);

    void updateUser(User u);

    int findNumberOfUsers();

    void deleteUser(int i);

    int getMaxValueFromColumn(String columnName);

    int getNextUsableUserId();

    List<Comment> findCommentsByUserId(int id, PaginationData pagination);
}
