package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests
@RestController
@CrossOrigin
public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;


    // create an Autowired controller to inject the categoryDao and ProductDao
    @Autowired
    public CategoriesController(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    // add the appropriate annotation for a get action
    @RequestMapping(path="/categories", method = RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public List<Category> getAllCategories() {
        // find and return all categories
        System.out.println("View All Categories..");
        return this.categoryDao.getAllCategories();
    }

    // add the appropriate annotation for a get action
    @RequestMapping(path="/categories/{Id}", method = RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public Category getCategoriesById(@PathVariable int Id) {
        // get the category by id
        return this.categoryDao.getById(Id);
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @RequestMapping(path = "/categories/{categoryId}/products", method = RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public List<Product> getAllByIdCat(@PathVariable Category categoryId) {
        // get a list of product by categoryId
        return this.categoryDao.getAllByCat(categoryId);
    }

    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    @RequestMapping(path = "/categories", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category addCategory(@RequestBody Category categories) {
        // insert the category
        return this.categoryDao.create(categories);
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @RequestMapping(path = "/categories/{categoryId}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCategory(@PathVariable int categoryId, @RequestBody Category category) {
        // update the category by categoryId

    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @RequestMapping(path = "/{categoryId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategory(@PathVariable int id){
        // delete the category by id

    }
}
