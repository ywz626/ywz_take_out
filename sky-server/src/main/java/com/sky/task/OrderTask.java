package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 于汶泽
 */
@Slf4j
@Component
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * *")
    public void OutTimeOrder(){
        LocalDateTime time = LocalDateTime.now().minusMinutes(15);
        List<Orders> orderlist = orderMapper.selectByStatusAndOrderTime(Orders.PENDING_PAYMENT,time);

        if(orderlist!=null && orderlist.size()>0){
            for(Orders order:orderlist){
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
                log.info("order:{} 因超时被删除",order);
            }
        }
        log.info("自动执行任务每分钟执行一次");
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void OutDeliveryTime(){
        List<Orders> list = orderMapper.selectBystatusOnDelivery();
        if(list!=null && list.size()>0){
            for(Orders order:list){
                order.setStatus(Orders.COMPLETED);
                order.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }
}
