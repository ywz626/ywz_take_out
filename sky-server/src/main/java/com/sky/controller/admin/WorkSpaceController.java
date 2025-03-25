package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 于汶泽
 */
@RequestMapping("/admin/workspace")
@RestController
public class WorkSpaceController {
    @Autowired
    private WorkSpaceService workSpaceService;

    /**
     * 工作台查看今日数据
     * @return
     */
    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessData() {
        return Result.success(workSpaceService.businessData());
    }

    /**
     * 工作台查看订单详情
     * @return
     */
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> overviewOrders() {
        return Result.success(workSpaceService.overviewOrders());
    }

    /**
     * 查询菜品总览
     * @return
     */
    @GetMapping("overviewDishes")
    public Result<DishOverViewVO> overviewDishes() {
        return Result.success(workSpaceService.overviewDishes());
    }

    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> overviewSetmeals() {
        return Result.success(workSpaceService.overviewSetmeals());
    }
}
