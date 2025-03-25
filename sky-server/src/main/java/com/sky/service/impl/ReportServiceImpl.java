package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 于汶泽
 */
@Slf4j
@Service
public class ReportServiceImpl implements ReportService {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 数据统计营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO totalTurnover(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        while (!begin.isAfter(end)) {
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        List<Double> totalTurnover = new ArrayList<>();
        for (LocalDate date : dateList) {
            Map map = new HashMap();
            map.put("status", Orders.COMPLETED);
            map.put("begin", LocalDateTime.of(date, LocalTime.MIN));
            map.put("end", LocalDateTime.of(date, LocalTime.MAX));
            log.info("map:{}", map);
            Double turnover = orderMapper.getTotalTurnover(map);
            if(turnover != null) {
                totalTurnover.add(turnover);
            }
            else {
                totalTurnover.add(0.0);
            }
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(totalTurnover,","))
                .build();
    }

    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        while (!begin.isAfter(end)) {
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        List<Double> newUserList = new ArrayList<>();
        List<Double> totalUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            Map map = new HashMap();
            map.put("begin", LocalDateTime.of(date, LocalTime.MIN));
            map.put("end", LocalDateTime.of(date, LocalTime.MAX));
            Double newUserCount = userMapper.getNewUserCount(map);
            Double totalUserCount = userMapper.getTotalUserCount(map);
            if(newUserCount != null) {
                newUserList.add(newUserCount);
            }else {
                newUserList.add(0.0);
            }
            if(totalUserCount != null) {
                totalUserList.add(totalUserCount);
            }else {
                totalUserList.add(0.0);
            }
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .build();
    }

    @Override
    public OrderReportVO orderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        while (!begin.isAfter(end)) {
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        List<Integer> allOrders = new ArrayList<>();
        List<Integer> effectiveOrders = new ArrayList<>();
        Integer allOrderCount = orderMapper.countOrders();
        Integer effectiveOrderCount = orderMapper.countEffectiveOrders(Orders.COMPLETED);
        for (LocalDate date : dateList) {
            Map map = new HashMap();
            map.put("status", Orders.COMPLETED);
            map.put("begin", LocalDateTime.of(date, LocalTime.MIN));
            map.put("end", LocalDateTime.of(date, LocalTime.MAX));
            Integer Orders = orderMapper.countOneDayOrders(map);
            Integer EffectiveOrders = orderMapper.countOneDayEffectiveOrders(map);
            if (Orders == null){
                Orders = 0;
            }
            if (EffectiveOrders == null){
                EffectiveOrders = 0;
            }
            allOrders.add(Orders);
            effectiveOrders.add(EffectiveOrders);
        }
        Double pow = (double) effectiveOrderCount/allOrderCount;
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .totalOrderCount(allOrderCount)
                .orderCompletionRate(pow)
                .validOrderCountList(StringUtils.join(effectiveOrders,","))
                .orderCountList(StringUtils.join(allOrders,","))
                .validOrderCount(effectiveOrderCount)
                .build();
    }

    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        List<String> nameList = new ArrayList<>();
        List<String> numberList = new ArrayList<>();
        Map map = new HashMap();
        map.put("status", Orders.COMPLETED);
        map.put("begin", LocalDateTime.of(begin, LocalTime.MIN));
        map.put("end", LocalDateTime.of(end, LocalTime.MAX));

        nameList = orderMapper.salesTop10Name(map);
        numberList = orderMapper.salesTop10Num(map);
        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList,","))
                .numberList(StringUtils.join(numberList,","))
                .build();
    }
}
