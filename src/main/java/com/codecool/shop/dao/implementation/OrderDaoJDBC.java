package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoJDBC implements OrderDao{

    private static OrderDaoJDBC instance = null;

    public static OrderDaoJDBC getInstance() {
        if (instance == null) {
            instance = new OrderDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Order order) {
        String query = "INSERT INTO orders (status, user_id) VALUES('" + order.getStatus()  + "', '" +
                        order.getUser().getId() + "');";
        executeQuery(query);

    }

    @Override
    public Order find(int id) {
        String query = "SELECT * FROM orders WHERE status = 'New' AND id ='" + id + "';";

        UserDao users = UserDaoJDBC.getInstance();

        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){
            if (resultSet.next()){
                Order result = new Order(resultSet.getInt("id"),"Order", resultSet.getString("status"),
                        users.find(resultSet.getInt("user_id")));

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
        String query = String.format("DELETE FROM orders WHERE id=%s", id);
        executeQuery(query);
    }

    @Override
    public void setStatus(int id, String status){
        String query = "UPDATE orders SET status='"+ status + "' WHERE id='"+ id +"';";
        executeQuery(query);
    }

    @Override
    public List<Order> getAll() {
        String query = "SELECT * FROM orders;";

        UserDao users = UserDaoJDBC.getInstance();

        List<Order> orderList = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){
            while (resultSet.next()){
                Order result = new Order(resultSet.getInt("id"), "order", resultSet.getString("status"),
                        users.find(resultSet.getInt("user_id")));

                orderList.add(result);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }

    public Order getByUser(int userId){

        String query = "SELECT * FROM orders WHERE status = 'New' AND user_id ='" + userId + "';";

        UserDao users = UserDaoJDBC.getInstance();

        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){
            if (resultSet.next()){
                Order result = new Order(resultSet.getInt("id"),"Order", resultSet.getString("status"),
                        users.find(resultSet.getInt("user_id")));

                return result;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

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
