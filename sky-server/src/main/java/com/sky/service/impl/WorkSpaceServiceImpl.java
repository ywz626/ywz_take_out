package com.sky.service.impl;

import com.sky.entity.Dish;
import com.sky.entity.Orders;
import com.sky.entity.Setmeal;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 于汶泽
 */
@Slf4j
@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 工作台显示今日数据
     * @return
     */
    @Override
    public BusinessDataVO businessData() {
        Map map = new HashMap();
        map.put("begin", LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        map.put("end", LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        Integer newUsers = userMapper.getNewUserCount(map);
        Integer orders = orderMapper.countOrdersByStatus(map);
        map.put("status", Orders.COMPLETED);
        Integer effectiveOrders = orderMapper.countOrdersByStatus(map);
        Double Rate = (double) effectiveOrders/orders;
        Double totalTurnover = orderMapper.getTotalTurnover(map);
        log.info("total turnover: " + totalTurnover);
        log.info("Rate : " + Rate);
        Double unitPrice = totalTurnover / effectiveOrders;
        return BusinessDataVO.builder()
                .newUsers(newUsers)
                .orderCompletionRate(Rate)
                .turnover(totalTurnover)
                .unitPrice(unitPrice)
                .validOrderCount(effectiveOrders)
                .build();
    }

    /**
     * 查询订单管理数据
     * @return
     */
    @Override
    public OrderOverViewVO overviewOrders() {
        Map map = new HashMap();
        map.put("begin", LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        map.put("end", LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        Integer allOrders = orderMapper.countOrdersByStatus(map);
        map.put("status", Orders.CANCELLED);
        Integer cancelOrders = orderMapper.countOrdersByStatus(map);
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.countOrdersByStatus(map);
        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.countOrdersByStatus(map);
        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer confirmedOrders = orderMapper.countOrdersByStatus(map);
        return OrderOverViewVO.builder()
                .allOrders(allOrders)
                .cancelledOrders(cancelOrders)
                .completedOrders(completedOrders)
                .deliveredOrders(deliveredOrders)
                .waitingOrders(confirmedOrders)
                .build();
    }

    /**
     * 查询菜品总览
     * @return
     */
    @Override
    public DishOverViewVO overviewDishes() {
        Integer discontinued = dishMapper.getDishcont(Dish.DISCONT_ONE);
        Integer sold = dishMapper.getDishcont(Dish.DISCONT_ZERO);
        return DishOverViewVO.builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
    }

    @Override
    public SetmealOverViewVO overviewSetmeals() {
        Integer discontinued = setmealMapper.getSetmealCount(Setmeal.STATUS_NO);
        Integer sold = setmealMapper.getSetmealCount(Setmeal.STATUS_YES);
        return SetmealOverViewVO.builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
    }
}
