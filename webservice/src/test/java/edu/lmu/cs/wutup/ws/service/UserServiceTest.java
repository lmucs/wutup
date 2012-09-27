package edu.lmu.cs.wutup.ws.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.dao.UserDao;
import edu.lmu.cs.wutup.ws.model.User;

public class UserServiceTest {

    UserServiceImpl service;
    UserDao dao;

    User sampleUser = new User(1, "Alice");

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

    // TODO - WAY MORE TO DO

}
