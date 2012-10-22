package edu.lmu.cs.wutup.ws.dao;

import edu.lmu.cs.wutup.ws.model.Category;

public interface CategoryDao {

    void createCategory(Category c);
    
    Category findCategoryById(int id);

    void updateCategory(Category c);

    int findNumberOfCategories();

    void deleteCategory(int id);

    int getMaxValueFromColumn(String columnName);

    int getNextUsableCategoryId();
    
}
