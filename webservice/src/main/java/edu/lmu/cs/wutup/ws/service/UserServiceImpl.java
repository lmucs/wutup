package edu.lmu.cs.wutup.ws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.dao.UserDao;

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
    public void deleteUser(int id) {
        userDao.deleteUser(id);
    }
    
    public User createUserAsUpdateTemplate(User userToUpdate, User updater) {
        User template = new User(
                userToUpdate.getId(),
                updater.getFirstName() == null ? userToUpdate.getFirstName() : updater.getFirstName(),
                updater.getLastName() == null ? userToUpdate.getLastName() : updater.getLastName(),
                updater.getEmail() == null ? userToUpdate.getEmail() : updater.getEmail(),
                updater.getNickname() == null ? userToUpdate.getNickname() : updater.getNickname());
        return template;
    }
}
