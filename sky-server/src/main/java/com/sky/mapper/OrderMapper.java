package com.sky.mapper;

import com.sky.dto.*;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.vo.OrderListVO;
import com.sky.vo.SalesTop10ReportVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author 于汶泽
 */
@Mapper
public interface OrderMapper {


    void saveOrder(Orders orders);


    @Select("select * from orders where number=#{orderId} and user_id=#{currentId}")
    Orders getBy(String orderId, Long currentId);

    void update(Orders order);

    @Select("select * from orders where user_id=#{userId} order by order_time desc")
    List<OrderListVO> listOrder(Long userId);

    @Select("select * from order_detail where order_id=#{id}")
    List<OrderDetail> listDetail(Long id);

    @Select("select * from orders where id=#{id} and user_id=#{userId}")
    OrderListVO detail(Long id, Long userId);

    @Delete("delete from orders where id=#{id}")
    void deleteOrder(Long id);

    @Delete("delete from order_detail where order_id=#{id}")
    void deleteDish(Long id);



    List<OrderListVO> adminList(OrdersPageQueryDTO dto);


    @Select("select count(*) from orders where status=2")
    Integer countWaitOrder();

    @Select("select count(*) from orders where status=3")
    Integer countWaitDelivery();

    @Select("select count(*) from orders where status=4")
    Integer countDelivering();

    @Select("select * from orders where id=#{id}")
    OrderListVO adminDetail(Long id);

    @Update("update orders set status=#{status} where id=#{id}")
    void updateStatus(OrdersConfirmDTO dto);

    @Update("update orders set rejection_reason=#{rejectionReason}, status=6,cancel_time=#{cancelTime} where id=#{id}")
    void rejectOrder(OrdersRejectionDTO dto);

    @Update("update orders set cancel_reason=#{cancelReason},status=6,cancel_time=#{cancelTime} where id=#{id}")
    void cancelOrder(OrdersCancelDTO dto);

    @Update("update orders set status=4 where id=#{id}")
    void delivery(Long id);

    @Update("update orders set status=5 where id=#{id}")
    void compeleteOrder(Long id);

    @Select("select * from orders where status=#{pendingPayment} and order_time<#{time}")
    List<Orders> selectByStatusAndOrderTime(Integer pendingPayment, LocalDateTime time);

    @Update("update orders set status=#{cancelled}")
    void updateStatusByOutTime(Integer cancelled);

    @Select("select * from orders where status=4")
    List<Orders> selectBystatusOnDelivery();

    Double getTotalTurnover(Map map);

    @Select("select count(*) from orders")
    Integer countOrders();

    @Select("select count(*) from orders where status=#{status}")
    Integer countEffectiveOrders(Integer status);

    @Select("select count(*) from orders where order_time between #{begin} and #{end}")
    Integer countOneDayOrders(Map map);

    @Select("select count(*) from orders where status=#{status} and order_time between #{begin} and #{end}")
    Integer countOneDayEffectiveOrders(Map map);

    @Select("select name nameList  from order_detail join orders on order_detail.order_id = orders.id and status=#{status} " +
            "where order_time between #{begin} and #{end} "+
            "group by name order by sum(order_detail.number) desc limit 0,10")
    List<String> salesTop10Name(Map map);

    @Select("select sum(order_detail.number) numberList from order_detail join orders on order_detail.order_id = orders.id and status=#{status} " +
            "where order_time between #{begin} and #{end} "+
            "group by name order by sum(order_detail.number) desc limit 0,10")
    List<String> salesTop10Num(Map map);
}

