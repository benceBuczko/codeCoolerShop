package com.codecool.shop.dao;

import com.codecool.shop.dao.implementation.ProductCategoryDaoJDBC;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryDaoTest {

    private ProductCategoryDao productCategoryDao = ProductCategoryDaoJDBC.getInstance();

    @Test
    void find() {
        assertEquals(1, productCategoryDao.find(1).getId());
    }

}