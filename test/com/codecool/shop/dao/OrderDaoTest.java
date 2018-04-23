package com.codecool.shop.dao;

import com.codecool.shop.dao.implementation.OrderDaoJDBC;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderDaoTest {

    private OrderDao  orderDao = OrderDaoJDBC.getInstance();

    @Test
    void find() {
        assertEquals(1, orderDao.find(1).getId());
    }

    @Test
    void setStatus() {
        String before = orderDao.find(1).getStatus();
        orderDao.setStatus(1, "New");
        String after = orderDao.find(1).getStatus();
        assertEquals(before, after);
    }

    @Test
    void getAll() {
        int orders = orderDao.getAll().size();
        assertEquals(1, orders);
    }
}