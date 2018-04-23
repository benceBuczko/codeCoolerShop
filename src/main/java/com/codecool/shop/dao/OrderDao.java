package com.codecool.shop.dao;

import com.codecool.shop.model.Order;

import java.util.List;

public interface OrderDao {

    void add(Order order);
    Order find(int id);
    void remove(int id);

    void setStatus(int id, String status);
    List<Order> getAll();
    Order getByUser(int userId);
}
