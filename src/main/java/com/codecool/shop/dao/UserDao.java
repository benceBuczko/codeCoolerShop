package com.codecool.shop.dao;

import com.codecool.shop.model.User;

import java.util.List;

public interface UserDao {
    void add(User product);
    User find(int id);
    void remove(int id);

    List<User> getAll();
    User getByName(String userName);
}
