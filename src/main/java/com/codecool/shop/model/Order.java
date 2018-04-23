package com.codecool.shop.model;

import java.util.ArrayList;

public class Order extends BaseModel {
    private ArrayList<LineItem> products;
    private String status;
    private User user;

    public Order(int id, String name, String status, User user) {
        super(name);
        this.id = id;
        this.products = new ArrayList<>();
        this.status = status;
        this.user = user;
    }

    public void setProducts(ArrayList<LineItem> products) {
        this.products = products;
    }

    public ArrayList getProducts() {
        return this.products;
    }

    public void addProduct(LineItem product) {
        this.products.add(product);
    }

    public String toString() {
        return String.format("id: %1$d, " +
                        "name: %2$s, " +
                        "description: %3$s",
                this.id,
                this.name,
                this.description
        );
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
