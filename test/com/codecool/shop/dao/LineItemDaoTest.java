package com.codecool.shop.dao;

import com.codecool.shop.dao.implementation.LineItemDaoJDBC;

import static org.junit.jupiter.api.Assertions.*;

class LineItemDaoTest {

    private LineItemDao lineItemDao = LineItemDaoJDBC.getInstance();

    @org.junit.jupiter.api.Test
    void find() {
        assertEquals(7, lineItemDao.find(7).getId());
    }

    @org.junit.jupiter.api.Test
    void increaseQuantity() {
        int before = lineItemDao.find(7).getQuantity();
        lineItemDao.increaseQuantity(7);
        int after = lineItemDao.find(7).getQuantity();
        assertEquals(before + 1, after);
    }

}