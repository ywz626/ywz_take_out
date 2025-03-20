package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @author 于汶泽
 */
public interface CategoryService {

    PageResult CategoryPage(CategoryPageQueryDTO cDto);

    void saveCategory(CategoryDTO category);

    void updateCategory(CategoryDTO categoryDTO);

    void updateStatus(Integer status, Long id);

    void deleteCategory(Integer id);

    List<Category> getList(Integer type);
}
