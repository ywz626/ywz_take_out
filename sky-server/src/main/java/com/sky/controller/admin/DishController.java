package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 于汶泽
 */
@RequestMapping("admin/dish")
@RestController
public class DishController {
    @Autowired
    private DishService dishService;

    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO) {
        dishService.update(dishDTO);
        return Result.success();
    }

    /**
     * 修改菜品时回显id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishVO> getDishById(@PathVariable Long id) {
        return Result.success(dishService.getById(id));
    }

    /**
     * 修改菜品状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result updateDishStatus(@PathVariable Integer status,Long id) {
        dishService.updateStatus(status,id);
        return Result.success();
    }

    /**
     * 分页查询
     * @param dto
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> pageDish(DishPageQueryDTO dto) {
        return Result.success(dishService.pageDish(dto));
    }

    /**
     * 新增菜品
     * @param dish
     * @return
     */
    @PostMapping
    public Result saveDish(@RequestBody DishDTO dish) {
        dishService.saveDish(dish);
        return Result.success();
    }

    @DeleteMapping
    public Result deleteDish(@RequestParam("ids") List<Long> ids) {
        dishService.delete(ids);
        return Result.success();
    }
}
