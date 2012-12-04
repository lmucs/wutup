package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.lmu.cs.wutup.ws.dao.UserDao;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public void updateUser(User u) {
        userDao.updateUser(u);
    }

    @Override
    public void createUser(User u) {
        userDao.createUser(u);
    }

    @Override
    public User findUserById(int id) {
        return userDao.findUserById(id);
    }
    
    @Override
    public User findUserBySessionId(String sessionId) {
        return userDao.findUserBySessionId(sessionId);
    }

    @Override
    public User findUserByFacebookId(String id) {
        return userDao.findUserByFacebookId(id);
    }

    @Override
    public void deleteUser(int id) {
        userDao.deleteUser(id);
    }

    @Override
    public List<Comment> findCommentsByUser(User author, PaginationData pagination) {
        return userDao.findCommentsByUser(author, pagination);
    }

    public User createUserAsUpdateTemplate(User userToUpdate, User updater) {
        User template = new User(
                userToUpdate.getId(),
                updater.getFirstName() == null ? userToUpdate.getFirstName() : updater.getFirstName(),
                updater.getLastName() == null ? userToUpdate.getLastName() : updater.getLastName(),
                updater.getEmail() == null ? userToUpdate.getEmail() : updater.getEmail(),
                updater.getNickname() == null ? userToUpdate.getNickname() : updater.getNickname(),
                updater.getSessionId() == null ? userToUpdate.getSessionId() : updater.getSessionId());
        return template;
    }
}
