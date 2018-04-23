package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.LineItemDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LineItemDaoJDBC implements LineItemDao {

    private static LineItemDaoJDBC instance = null;

    public static LineItemDaoJDBC getInstance() {
        if (instance == null) {
            instance = new LineItemDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(LineItem lineItem) {
        String query = "INSERT INTO lineitem (quantity, order_id, product_id) VALUES('" +
                        lineItem.getQuantity() + "', '" + lineItem.getOrder().getId() + "', '" +
                        lineItem.getProduct().getId() + "');";
        executeQuery(query);
    }

    @Override
    public LineItem find(int id) {
        String query = "SELECT * FROM lineitem WHERE id ='" + id + "';";

        OrderDao orders = OrderDaoJDBC.getInstance();
        ProductDao products = ProductDaoJDBC.getInstance();

        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){
            if (resultSet.next()){
                LineItem result = new LineItem(resultSet.getInt("id"), products.find(resultSet.getInt("product_id")).getName(), products.find(resultSet.getInt("product_id")).getDefaultPrice(), resultSet.getInt("quantity"),
                        orders.find(resultSet.getInt("order_id")), products.find(resultSet.getInt("product_id")));

                return result;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void remove(int id) {
        String query = String.format("DELETE FROM lineitem WHERE id=%s", id);
        executeQuery(query);
    }

    @Override
    public void removeAll(){
        String query = "DELETE FROM lineitem";
        executeQuery(query);
    }

    @Override
    public
    List<LineItem> getBy(Order order) {

        String query = "SELECT * FROM lineitem WHERE order_id='" + order.getId() + "';";

        OrderDao orders = OrderDaoJDBC.getInstance();
        ProductDao products = ProductDaoJDBC.getInstance();

        List<LineItem> lineItems = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){
            while (resultSet.next()){
                LineItem result = new LineItem(resultSet.getInt("id"), products.find(resultSet.getInt("product_id")).getName(), products.find(resultSet.getInt("product_id")).getDefaultPrice(), resultSet.getInt("quantity"),
                        orders.find(resultSet.getInt("order_id")), products.find(resultSet.getInt("product_id")));

                lineItems.add(result);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lineItems;
    }

    @Override
    public void increaseQuantity(int id){
        String query = "UPDATE lineitem SET quantity = quantity + 1 WHERE id='" + id +"';";
        executeQuery(query);
    }

    @Override
    public void setQuantity(int id, int quantity){
        String query = "UPDATE lineitem SET quantity='"+ quantity +"' WHERE id='" + id +"';";
        executeQuery(query);
    }

    private
    Connection getConnection() throws SQLException {
        return DataConnection.getInstance().getConnection();
    }

    private void executeQuery(String query) {
        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
        ){
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
