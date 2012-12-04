package edu.lmu.cs.wutup.ws.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.dao.UserDao;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;

public class UserServiceTest {

    UserServiceImpl service;
    UserDao dao;

    User sampleUser = new User(1, "Alice");
    PaginationData samplePagination = new PaginationData(0, 10);

    @Before
    public void setUp() {
        service = new UserServiceImpl();
        dao = mock(UserDao.class);
        service.userDao = dao;
    }

    @Test
    public void creationDelegatesToDao() {
        service.createUser(sampleUser);
        verify(dao).createUser(sampleUser);
    }

    @Test
    public void updateDelegatesToDao() {
        service.updateUser(sampleUser);
        verify(dao).updateUser(sampleUser);
    }

    @Test
    public void deleteDelegatesToDao() {
        service.deleteUser(sampleUser.getId());
        verify(dao).deleteUser(sampleUser.getId());
    }

    @Test
    public void findByIdDelegatesToDao() {
        service.findUserById(sampleUser.getId());
        verify(dao).findUserById(sampleUser.getId());
    }

    @Test
    public void findCommentsByUserIdDelegatsToDao() {
        service.findCommentsByUserId(sampleUser.getId(), samplePagination);
        verify(dao).findCommentsByUserId(sampleUser.getId(), samplePagination);
    }

}
