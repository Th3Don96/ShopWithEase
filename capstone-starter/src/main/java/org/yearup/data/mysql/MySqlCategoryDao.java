package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao{
    private DataSource dataSource;
    private List<Category> categories;
    private List<Product> products;
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
        this.categories = new ArrayList<>();
        this.products = new ArrayList<>();
    }

    @Override
    public List<Category> getAllCategories() {
        this.categories.clear();
        String sql = "SELECT * FROM categories;";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rows = statement.executeQuery();
            while(rows.next()){
                this.categories.add(new Category(rows.getInt(1), rows.getString(2), rows.getString(3)));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return this.categories;
    }
    @Override
    public List<Product> getAllByCat(Category category) {
        // get all products by categories
        this.products.clear();
        String sql = "SELECT * FROM products WHERE categoryId = ?;";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, category.getCategoryId());
            ResultSet rows = statement.executeQuery();
            while(rows.next()){
                this.products.add(new Product(rows.getInt(1), rows.getString(2), rows.getBigDecimal(3),
                        rows.getInt(4), rows.getString(5), rows.getString(6), rows.getInt(7), rows.getBoolean(8), rows.getString(9)));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return this.products;
    }


    @Override
    public Category getById(int categoryId) {
        // get category by id
        Category category = null;
        String sql = "SELECT * FROM categories WHERE categoryId = ?;";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,categoryId);
            ResultSet rows = statement.executeQuery();
            while(rows.next()){
                category = new Category(rows.getInt(1), rows.getString(2), rows.getString(3));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public Category create(Category category) {
        String sql = "INSERT INTO products(category_id, name, description) VALUES (?, ?, ?);";
        try (Connection connection = dataSource.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, category.getCategoryId());
            statement.setString(2, category.getCategoryName());
            statement.setString(3, category.getDescription());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Retrieve the auto-incremented ID
                    int orderId = generatedKeys.getInt(1);

                    // get the newly inserted category
                    return this.getById(orderId);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(int categoryId, Category category) {

            String sql = "UPDATE categories SET name = ? , description = ? WHERE category_id = ?";
            try (Connection connection = this.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql))
            {
                statement.setString(1, category.getCategoryName());
                statement.setString(2, category.getDescription());
                statement.setInt(3, categoryId);


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    @Override
    public void delete(int categoryId) {
        // delete category
        String sql = "DELETE * FROM categories WHERE category_id = ?;";

        try (Connection connection = dataSource.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
