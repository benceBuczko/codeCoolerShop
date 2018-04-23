package com.codecool.shop.dao;

import com.codecool.shop.dao.implementation.UserDaoJDBC;
import org.junit.jupiter.api.Test;

import javax.jws.soap.SOAPBinding;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    private UserDao userDao = UserDaoJDBC.getInstance();

    @Test
    void find() {
        assertEquals(1, userDao.find(1).getId());
    }

    @Test
    void getAll() {
        int users = userDao.getAll().size();
        assertEquals(1, users);
    }
}