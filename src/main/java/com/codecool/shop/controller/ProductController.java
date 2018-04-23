package com.codecool.shop.controller;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;

import com.codecool.shop.model.Order;
import spark.Request;
import spark.Response;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class ProductController {

    public static ModelAndView renderProducts(Request req, Response res, String html) {
        ProductDao productDataStore = ProductDaoJDBC.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
        OrderDao orderDataStore = OrderDaoJDBC.getInstance();
        LineItemDao lineItemDataStore = LineItemDaoJDBC.getInstance();
        UserDao userDataStore = UserDaoJDBC.getInstance();
        int userId;

        Map params = new HashMap<>();
        params.put("categories", productCategoryDataStore.getAll());
        params.put("boys", productDataStore.getBy(productCategoryDataStore.find(1)));
        params.put("girls", productDataStore.getBy(productCategoryDataStore.find(2)));
        params.put("products", productDataStore.getAll());
        if (req.session().attribute("user") != null) {
            userId = req.session().attribute("user");
            Order order = orderDataStore.getByUser(userId);
            params.put("user",userDataStore.find(userId));
            params.put("orders", order);
            params.put("lineitems", lineItemDataStore.getBy(order));
        } else {
            params.put("user",null);
            params.put("orders", null);
            params.put("lineitems", null);
        }
        return new ModelAndView(params, html);
    }

}
