package com.codecool.shop.dao;

import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;

import java.util.List;

public interface LineItemDao {

    void add(LineItem lineItem);
    LineItem find(int id);
    void remove(int id);
    void removeAll();

    void increaseQuantity(int id);
    void setQuantity(int id, int quantity);
    List<LineItem> getBy(Order order);
}
