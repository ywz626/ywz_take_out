package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 于汶泽
 */
@RequestMapping("/user/setmeal")
@RestController("userSetmealController")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类id查询套餐
     * @param id
     * @return
     */
    @Cacheable(value = "setmeal" ,key="#id")
    @GetMapping("/list")
    public Result<List<Setmeal>> listDish(@RequestParam("categoryId") Long id) {
        return Result.success(setmealService.listSetmeal(id));
    }

    /**
     * 根据套餐id查询套餐菜品
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    public Result<List<SetmealDish>> listDishById(@PathVariable Long id) {
        return Result.success(setmealService.listDish(id));
    }
}
