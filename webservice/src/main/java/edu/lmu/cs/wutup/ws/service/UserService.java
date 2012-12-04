package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;

public interface UserService {

    void updateUser(User u);

    void createUser(User u);

    User findUserById(int id);

    User findUserByFacebookId(String id);

    User findUserBySessionId(String id);

    void deleteUser(int id);

    List<Comment> findCommentsByUser(User author, PaginationData pagination);
}
