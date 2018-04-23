package com.codecool.shop.model;

public class LineItem extends BaseModel {

    private int quantity;
    private float price;
    private Order order;
    private Product product;


    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public LineItem(int id, String name, float price, int quantity, Order order, Product product) {
        super(name);

        this.price = price;
        this.id = id;
        this.quantity = quantity;
        this.order = order;
        this.product = product;
    }

    public
    Order getOrder() {
        return order;
    }

    public
    void setOrder(Order order) {
        this.order = order;
    }

    public
    Product getProduct() {
        return product;
    }

    public
    void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }


    public void increaseQuantity() {
        this.quantity++;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("id: %1$d, " +
                        "name: %2$s, ",
                this.id,
                this.name);
    }
};
