package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Here you can find JDBC related queries for the category table.
 * Implemented queries for the category table are: add, find by id,
 * remove by id and get all of the categories.
 */

public class ProductCategoryDaoJDBC implements ProductCategoryDao {

    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryDao.class);

    private static ProductCategoryDaoJDBC instance = null;

    /**
     * A private Constructor prevents any other class from instantiating.
     */

    public static ProductCategoryDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoJDBC();
        }
        return instance;
    }

    /**
     * Add a new category for the category table.
     *
     * @param category
     */

    @Override
    public void add(ProductCategory category) {
        logger.debug("{} added to the category table", category.getName());

        String query = "INSERT INTO category (name) " +
                "VALUES ('" + category.getName() + "');";
        executeQuery(query);
    }

    /**
     * Find a specific product category from the category table by a given id.
     *
     * @param id
     * @return Return the category if found in the category table.
     */

    @Override
    public ProductCategory find(int id) {

        logger.debug("Select a product category with the given id: {}", id);

        String query = "SELECT * FROM category WHERE id ='" + id + "';";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            if (resultSet.next()) {
                logger.debug("Product category has been found.");
                ProductCategory result = new ProductCategory(resultSet.getInt("id"), resultSet.getString("name"), "HR", "cucc");
                return result;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.warn("Connection failed. StackTrace: {}", e.getMessage());
        }
        return null;
    }

    /**
     * It removes a product category from the category table by the given id
     *
     * @param id
     */


    @Override
    public void remove(int id) {
        logger.debug("Delete a category with the given id: {}", id);

        String query = "DELETE FROM category WHERE id='" + id + "';";
        executeQuery(query);
    }

    /**
     * Return all of the categories from the category table
     *
     * @return ProductCategory type List
     */

    @Override
    public List<ProductCategory> getAll() {
        logger.debug("Select all from the category table.");
        String query = "SELECT * FROM category;";
        executeQuery(query);
        List<ProductCategory> categoryList = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                ProductCategory result = new ProductCategory(resultSet.getInt("id"), resultSet.getString("name"),
                        "HR",
                        "kk");

                categoryList.add(result);
            }

        } catch (SQLException e) {
            logger.warn("Connection failed. StackTrace: {}", e.getMessage());
            e.printStackTrace();
        }
        logger.debug("Return all of the product category.");
        return categoryList;
    }

    private Connection getConnection() throws SQLException {
        return DataConnection.getInstance().getConnection();
    }

    private void executeQuery(String query) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
        ) {
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
