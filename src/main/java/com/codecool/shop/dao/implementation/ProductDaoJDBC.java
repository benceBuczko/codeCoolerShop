package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Here you can find JDBC related queries for the products table.
 * implemented queries for the products table are: add, find by id,
 * remove by id and get all of the products or get by category/category and supplier.
 */

public class ProductDaoJDBC implements ProductDao {

    private final static Logger logger = LoggerFactory.getLogger( ProductDaoJDBC.class );

    private static ProductDaoJDBC instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductDaoJDBC() {
    }

    public static ProductDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductDaoJDBC();
        }
        return instance;
    }

    /**
     *  Add a new product to the products table.
      * @param product
     */

    @Override
    public void add(Product product) {
        logger.debug("Inserting {} to the products table.", product.getName());
        String query = "INSERT INTO product (name, description, price, currency, category_id, supplier_id) " +
                "VALUES ('" + product.getName() + "', '" + product.getDescription() + "', '" + product.getDefaultPrice() +
                "', '" + product.getDefaultCurrency() + "', '" + product.getProductCategory().getId() +
                "', '" + product.getSupplier().getId() + "');";
        executeQuery(query);
    }

    /**
     * Find a specific product by the given id.
     * @param id
     * @return the desired product by id.
     */

    @Override
    public Product find(int id) {
        logger.debug("Selecting a product by the id: {}.", id);
        String query = "SELECT * FROM product WHERE id ='" + id + "';";

        ProductCategoryDao categoryData = ProductCategoryDaoJDBC.getInstance();

        SupplierDao supplierData = SupplierDaoJDBC.getInstance();


        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            if (resultSet.next()) {
                logger.debug( "The item with id {} is selected.", id );
                Product result = new Product(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getFloat("price"),
                        resultSet.getString("currency"),
                        resultSet.getString("description"),
                        categoryData.find(resultSet.getInt("category_id")),
                        supplierData.find(resultSet.getInt("supplier_id")));


                return result;
            } else {
                return null;
            }

        } catch (SQLException e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Remove product by the given id.
     * @param id
     */

    @Override
    public void remove(int id) {
        logger.debug( "Delete product with id: {} from the category table.", id );
        String query = "DELETE FROM product WHERE id='" + id + "';";
        executeQuery(query);
    }

    /**
     * Return all of the products.
     * @return the products
     */

    @Override
    public List<Product> getAll() {
        logger.debug( "Select all of the products from the products table." );
        String query = "SELECT * FROM product;";
        executeQuery(query);
        ProductCategoryDao categoryData = ProductCategoryDaoJDBC.getInstance();

        SupplierDao supplierData = SupplierDaoJDBC.getInstance();

        List<Product> productListByCategory = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                Product result = new Product(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getFloat("price"),
                        resultSet.getString("currency"),
                        resultSet.getString("description"),
                        categoryData.find(resultSet.getInt("category_id")),
                        supplierData.find(resultSet.getInt("supplier_id")));

                productListByCategory.add(result);
            }

        } catch (SQLException e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
            e.printStackTrace();
        }
        logger.debug( "Successfully selected the products." );
        return productListByCategory;

    }

    /**
     * Select the products from the products table by the given supplier.
     * @param supplier
     * @return products with specific supplier
     */

    @Override
    public List<Product> getBy(Supplier supplier) {
        logger.debug("Select products by supplier:", supplier.getName());
        String query = "SELECT * FROM product WHERE supplier_id ='" + supplier.getId() + "';";
        executeQuery(query);
        ProductCategoryDao categoryData = ProductCategoryDaoJDBC.getInstance();

        SupplierDao supplierData = SupplierDaoJDBC.getInstance();

        List<Product> productListByCategory = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                Product result = new Product(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getFloat("price"),
                        resultSet.getString("currency"),
                        resultSet.getString("description"),
                        categoryData.find(resultSet.getInt("category_id")),
                        supplierData.find(resultSet.getInt("supplier_id")));

                productListByCategory.add(result);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        logger.debug( "Products successfully selected." );
        return productListByCategory;

    }

    /**
     * Select products by the given product category
     * @param productCategory
     * @return products with the given category
     */

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        logger.debug( "Selecting the products with product category: {}", productCategory.getName() );
        String query = "SELECT * FROM product WHERE category_id ='" + productCategory.getId() + "';";
        executeQuery(query);
        ProductCategoryDao categoryData = ProductCategoryDaoJDBC.getInstance();

        SupplierDao supplierData = SupplierDaoJDBC.getInstance();

        List<Product> productListByCategory = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                Product result = new Product(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getFloat("price"),
                        resultSet.getString("currency"),
                        resultSet.getString("description"),
                        categoryData.find(resultSet.getInt("category_id")),
                        supplierData.find(resultSet.getInt("supplier_id")));

                productListByCategory.add(result);
            }

        } catch (SQLException e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
            e.printStackTrace();
        }
        logger.debug( "Successfully selected the products." );
        return productListByCategory;

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
