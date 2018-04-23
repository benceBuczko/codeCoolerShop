package com.codecool.shop.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User class, has a constructor, getters and setters.
 */

public class User extends BaseModel {

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    private String email, password, shippinginfo, billinginfo;


    public User(int id, String name, String email, String password, String shippinginfo, String billinginfo) {
        super(name);
        logger.info("User created with name " + name);
        this.id = id;
        this.email = email;
        this.password = password;
        this.shippinginfo = shippinginfo;
        this.billinginfo = billinginfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShippinginfo() {
        return shippinginfo;
    }

    public void setShippinginfo(String shippinginfo) {
        this.shippinginfo = shippinginfo;
    }

    public String getBillinginfo() {
        return billinginfo;
    }

    public void setBillinginfo(String billinginfo) {
        this.billinginfo = billinginfo;
    }
}
