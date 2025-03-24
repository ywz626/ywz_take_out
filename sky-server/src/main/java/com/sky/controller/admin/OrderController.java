package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderListVO;
import com.sky.vo.OrderStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 于汶泽
 */
@RequestMapping("/admin/order")
@RestController("adminOrderController")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 管理端分页查询订单信息
     * @param dto
     * @return
     */
    @GetMapping("/conditionSearch")
    public Result<PageResult> conditionSearch(@ModelAttribute OrdersPageQueryDTO dto) {
        return Result.success(orderService.adminList(dto));
    }

    /**
     * 各个状态的订单统计
     * @return
     */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> adminStatusCount(){
        return Result.success(orderService.adminStatusCount());
    }

    /**
     * 查看订单详情
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    public Result<OrderListVO> orderDetail(@PathVariable Long id) {
        return Result.success(orderService.orderDetail(id));
    }

    /**
     * 管理端接单
     * @param dto
     * @return
     */
    @PutMapping("/confirm")
    public Result confirm(@RequestBody OrdersConfirmDTO dto) {
        orderService.confirmOrder(dto);
        return Result.success();
    }

    /**
     * 拒单
     * @param dto
     * @return
     */
    @PutMapping("/rejection")
    public Result rejection(@RequestBody OrdersRejectionDTO dto) {
        orderService.rejectionOrder(dto);
        return Result.success();
    }

    /**
     * 取消订单
     * @param dto
     * @return
     */
    @PutMapping("/cancel")
    public Result cancelOrder(@RequestBody OrdersCancelDTO dto){
        orderService.cancelOrder(dto);
        return Result.success();
    }

    /**
     * 派送订单
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    public Result deliveryOrder(@PathVariable Long id){
        orderService.deliveryOrder(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    public Result completeOrder(@PathVariable Long id){
        orderService.competeOrder(id);
        return Result.success();
    }
}
