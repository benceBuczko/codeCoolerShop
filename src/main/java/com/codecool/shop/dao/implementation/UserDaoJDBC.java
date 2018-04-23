package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.UserDao;
import com.codecool.shop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manipulates the data of user table in the database.
 * Singleton.
 */

public class UserDaoJDBC implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoJDBC.class);

    private static UserDaoJDBC instance = null;

    public static UserDaoJDBC getInstance() {
        if (instance == null) {
            instance = new UserDaoJDBC();
        }
        return instance;
    }

    /**
     * Adds a user to the database.
     * @param user Needs only an instance of the user class.
     */
    @Override
    public void add(User user) {
        String query = "INSERT INTO users (name, email, password, shipping_info, billing_info) VALUES ('"+
                user.getName() + "', '" + user.getEmail() + "', '" + user.getPassword() + "', '" + user.getShippinginfo()+ "', '" + user.getBillinginfo() + "');";
        executeQuery(query);
        logger.info("User added: " + user.getId());
    }

    /**
     * Gets the data for a specified user.
     * @param id
     * @return Returns the user if found in the database.
     */
    @Override
    public User find(int id) {
        String query = "SELECT * FROM users WHERE id ='" + id + "';";

        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){
            if (resultSet.next()){
                User result = new User(resultSet.getInt("id"),resultSet.getString("name"), resultSet.getString("email"),
                        resultSet.getString("password"), resultSet.getString("shipping_info"),
                        resultSet.getString("billing_info"));

                return result;
            } else {
                return null;
            }

        } catch (SQLException e) {
            logger.error("Query not executed");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Removes the specified user from the database.
     * @param id
     */
    @Override
    public void remove(int id) {
        String query = "DELETE FROM users WHERE id='" + id + "';";
        executeQuery(query);
        logger.info("User deleted: " + id);
    }

    /**
     * Returns all the users from the database.
     * @return
     */
    @Override
    public List<User> getAll() {
        String query = "SELECT * FROM users;";

        List<User> usersList = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){
            while (resultSet.next()){
                User result = new User(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("email"),
                        resultSet.getString("password"), resultSet.getString("shipping_info"),
                        resultSet.getString("billing_info"));

                usersList.add(result);
            }

        } catch (SQLException e) {
            logger.error("Query not executed");
            e.printStackTrace();
        }

        return usersList;
    }

    @Override
    public User getByName(String userName) {
        String query = "SELECT * FROM users WHERE name='" + userName + "';";

        List<User> usersList = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){
            if (resultSet.next()){
                User result = new User(resultSet.getInt("id"),resultSet.getString("name"), resultSet.getString("email"),
                        resultSet.getString("password"), resultSet.getString("shipping_info"),
                        resultSet.getString("billing_info"));

                return result;
            } else {
                return null;
            }

        } catch (SQLException e) {
            logger.error("Query not executed");
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
            logger.error("Query not executed");
            e.printStackTrace();
        }
    }
}
