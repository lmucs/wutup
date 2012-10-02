package edu.lmu.cs.wutup.ws.dao;

import edu.lmu.cs.wutup.ws.model.User;

public interface UserDao {

    void createUser(User u);
    User findUserById(int id);
    void updateUser(User u);
    int findNumberOfUsers();
    void deleteUser(User u);
    int getMaxValueFromColumn(String columnName);
    int getNextUsableUserId();
}
