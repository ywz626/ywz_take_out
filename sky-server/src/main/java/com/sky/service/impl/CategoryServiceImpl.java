package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 于汶泽
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public PageResult CategoryPage(CategoryPageQueryDTO cDto) {
        PageHelper.startPage(cDto.getPage(),cDto.getPageSize());
        Page cate = (Page) categoryMapper.CategoryPage(cDto);
        return new PageResult(cate.getTotal(),cate.getResult());
    }

    @Override
    public void saveCategory(CategoryDTO category) {
        Category cate = new Category();
        BeanUtils.copyProperties(category,cate);
        cate.setStatus(0);
//        cate.setCreateUser(BaseContext.getCurrentId());
//        cate.setUpdateUser(BaseContext.getCurrentId());
//        cate.setCreateTime(LocalDateTime.now());
//        cate.setUpdateTime(LocalDateTime.now());
        categoryMapper.saveCategory(cate);
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setStatus(0);
//        category.setUpdateUser(BaseContext.getCurrentId());
//        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.updateCategory(category);
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();
        categoryMapper.updateCategory(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        Integer count = dishMapper.getCountByCategoryId(id);
        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }else{
            categoryMapper.deleteCategory(id);
        }
    }

    @Override
    public List<Category> getList(Integer type) {
        return categoryMapper.getList(type);
    }
}
