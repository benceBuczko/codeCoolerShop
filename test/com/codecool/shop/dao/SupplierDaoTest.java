package com.codecool.shop.dao;

import com.codecool.shop.dao.implementation.SupplierDaoJDBC;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierDaoTest {

    private SupplierDao supplierDao = SupplierDaoJDBC.getInstance();

    @Test
    void find() {
        assertEquals(1, supplierDao.find(1).getId());
    }

    @Test
    void getAll() {
        int suppliers = supplierDao.getAll().size();
        assertEquals(12, suppliers);
    }
}