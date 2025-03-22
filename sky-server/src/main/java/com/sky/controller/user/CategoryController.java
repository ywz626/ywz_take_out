package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 于汶泽
 */
@RequestMapping("/user/category")
@RestController("userCategoryController")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取分类信息
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> listCategory(@RequestParam(value = "type",required = false) Integer type) {
        List<Category> list = categoryService.getList(type);
        return Result.success(list);
    }
}
