package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderListVO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;

import java.util.List;

/**
 * @author 于汶泽
 */
public interface OrderService {
    OrderSubmitVO submit(OrdersSubmitDTO dto);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;


    PageResult listHistory(OrdersPageQueryDTO dto);

    OrderListVO detail(Long id);

    void delete(Long id);

    void pardonOrder(Long id);

    PageResult adminList(OrdersPageQueryDTO dto);

    OrderStatisticsVO adminStatusCount();

    OrderListVO orderDetail(Long id);

    void confirmOrder(OrdersConfirmDTO dto);

    void rejectionOrder(OrdersRejectionDTO dto);

    void cancelOrder(OrdersCancelDTO dto);

    void deliveryOrder(Long id);

    void competeOrder(Long id);

    void reminder(Long id);
}
