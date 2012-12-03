package edu.lmu.cs.wutup.ws.service;

import edu.lmu.cs.wutup.ws.model.User;

public interface UserService {
    
    void updateUser(User u);
    void createUser(User u);
    User findUserById(int id);
    User findUserByFacebookId(String id);
    User findUserBySessionId(String id);
    void deleteUser(int id);
}
