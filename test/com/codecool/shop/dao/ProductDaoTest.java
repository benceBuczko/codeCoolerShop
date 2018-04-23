package com.codecool.shop.dao;

import com.codecool.shop.dao.implementation.ProductDaoJDBC;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoTest {

    private ProductDao productDao = ProductDaoJDBC.getInstance();

    @Test
    void find() {
        assertEquals(1, productDao.find(1).getId());
    }

    @Test
    void getAll() {
        int products = productDao.getAll().size();
        assertEquals(21, products);
    }
}