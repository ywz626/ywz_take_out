package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 于汶泽
 */
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    

    /**
     * 修改状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result updateSetmealStatus(@PathVariable("status") Integer status,Long id) {
        setmealService.updateStatus(status,id);
        return Result.success();
    }
    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    @PutMapping
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO) {
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 修改套餐信息时回显信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> getSetmealById(@PathVariable Long id) {
        return Result.success(setmealService.getById(id));
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result deleteSetmeal(@RequestParam("ids") List<Long> ids) {
        setmealService.delete(ids);
        return Result.success();
    }

    /**
     * 分页查询
     * @param setmeal
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> pageSetmeal(SetmealPageQueryDTO setmeal) {
        PageResult page = setmealService.pageSetmeal(setmeal);
        return Result.success(page);
    }

    /**
     * 新增套餐
     * @param setmeal
     * @return
     */
    @PostMapping
    public Result saveSetmeal(@RequestBody SetmealDTO setmeal) {
        setmealService.saveSetmeal(setmeal);
        return Result.success();
    }
}
