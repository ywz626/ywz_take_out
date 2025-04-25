package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 于汶泽
 *
 */
@RequiredArgsConstructor
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<Category>> categoryList(Integer type) {
        return Result.success(categoryService.getList(type));
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public Result deleteCategory(@RequestParam Integer id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }

    /**
     * 修改分类状态信息
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result updateCategoryStatus(@PathVariable Integer status, Long id){
        categoryService.updateStatus(status,id);
        return Result.success();
    }

    /**
     * 修改分类信息
     * @param categoryDTO
     * @return
     */
    @PutMapping
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public Result addCategory(@RequestBody CategoryDTO category) {
        categoryService.saveCategory(category);
        return Result.success();
    }

    /**
     * 分页查询
     * @param cDto
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> CategoryPage(CategoryPageQueryDTO cDto){
        return Result.success(categoryService.CategoryPage(cDto));
    }
    @RequestMapping(value = "/age",method = RequestMethod.GET)
    public Result test(){
        return Result.success();
    }
}
