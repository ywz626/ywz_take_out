package com.sky.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.properties.WeChatProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 于汶泽
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Value("${sky.shop.address}")
    private String shopAddress;
    @Value("${sky.baidu.ak}")
    private String ak;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderSubmitVO submit(OrdersSubmitDTO dto) {
        //获取地址数据
        AddressBook addressBook = addressBookMapper.getById(dto.getAddressBookId());
        checkAddress(addressBook.getProvinceName()+addressBook.getCityName()+addressBook.getDistrictName()+addressBook.getDetail());
        //获取用户数据
        User user = userMapper.getById(BaseContext.getCurrentId());
        //获取购物车列表数据
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.listCart(BaseContext.getCurrentId());
        //1.封装数据到orders
        Orders orders = new Orders();
        BeanUtils.copyProperties(dto, orders);
        orders.setStatus(1);
        orders.setNumber(System.currentTimeMillis() + "");
        orders.setUserId(BaseContext.getCurrentId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setPhone(addressBook.getPhone());
        orders.setPayStatus(0);
        orders.setAddress(addressBook.getDetail());
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orderMapper.saveOrder(orders);
        //2.封装数据到order_detail
        List<OrderDetail> orderDetail = new ArrayList();
        for (ShoppingCart shoppingCart : shoppingCarts) {
            OrderDetail ord = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, ord,"id");
            ord.setOrderId(orders.getId());
            orderDetail.add(ord);
        }
        orderDetailMapper.insertBatch(orderDetail);
        shoppingCartMapper.cleanCart(BaseContext.getCurrentId());
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderAmount(orders.getAmount())
                .orderNumber(orders.getNumber())
                .orderTime(orders.getOrderTime())
                .build();
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
//        // 当前登录用户id
//        Long userId = BaseContext.getCurrentId();
//        User user = userMapper.getById(userId);
//
//        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
//
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }
//
//        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
//        vo.setPackageStr(jsonObject.getString("package"));
        paySuccess(ordersPaymentDTO.getOrderNumber());
        return new OrderPaymentVO();
    }

    @Override
    public PageResult listHistory(OrdersPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(),dto.getPageSize());
        Long userId = BaseContext.getCurrentId();
        List<OrderListVO> orders = orderMapper.listOrder(userId);
        for (OrderListVO order : orders) {
            List<OrderDetail> orderDetail = new ArrayList<>();
            orderDetail = orderMapper.listDetail(order.getId());
            order.setOrderDetailList(orderDetail);
        }
        Page page = (Page) orders;
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public OrderListVO detail(Long id) {
        Long userId = BaseContext.getCurrentId();
        OrderListVO detail = orderMapper.detail(id, userId);
        List<OrderDetail> orderDetails = orderMapper.listDetail(id);
        detail.setOrderDetailList(orderDetails);
        return detail;
    }

    @Override
    public void delete(Long id) {
        orderMapper.deleteOrder(id);
        orderMapper.deleteDish(id);
    }

    @Override
    public void pardonOrder(Long id) {
        List<OrderDetail> orderDetails = orderMapper.listDetail(id);
        OrderListVO detail = orderMapper.detail(id, BaseContext.getCurrentId());
        Orders orders = new Orders();
        BeanUtils.copyProperties(detail, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setNumber(System.currentTimeMillis() + "");
        orders.setCheckoutTime(null);
        orders.setPayStatus(0);
        orders.setStatus(1);
        orders.setEstimatedDeliveryTime(LocalDateTime.now().plusHours(1));
        orderMapper.saveOrder(orders);
        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.setOrderId(orders.getId());
        }
        orderDetailMapper.insertBatch(orderDetails);
    }

    @Override
    public PageResult adminList(OrdersPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(),dto.getPageSize());
        List<OrderListVO> orderListVOS = orderMapper.adminList(dto);
//        for (OrderListVO orderListVO : orderListVOS) {
//            List<OrderDetail> orderDetails = orderMapper.listDetail(orderListVO.getId());
//            orderListVO.setOrderDetailList(orderDetails);
//        }
        for (OrderListVO orderListVO : orderListVOS) {
            List<String> dishes = orderDetailMapper.adminGetDishesByOrderId(orderListVO.getId());
            orderListVO.setOrderDishes(dishes);
        }

        Page page = (Page) orderListVOS;
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public OrderStatisticsVO adminStatusCount() {
        Integer waitOrder = orderMapper.countWaitOrder();
        Integer waitDelivery = orderMapper.countWaitDelivery();
        Integer delivering = orderMapper.countDelivering();
        return new OrderStatisticsVO(waitOrder,waitDelivery,delivering);
    }

    @Override
    public OrderListVO orderDetail(Long id) {
        OrderListVO orderListVO = orderMapper.adminDetail(id);
        List<OrderDetail> orderDetails = orderMapper.listDetail(id);
        orderListVO.setOrderDetailList(orderDetails);
        return orderListVO;
    }

    @Override
    public void confirmOrder(OrdersConfirmDTO dto) {
        dto.setStatus(3);
        orderMapper.updateStatus(dto);
    }

    @Override
    public void rejectionOrder(OrdersRejectionDTO dto) {
        dto.setCancelTime(LocalDateTime.now());
        orderMapper.rejectOrder(dto);
    }

    @Override
    public void cancelOrder(OrdersCancelDTO dto) {
        log.info("dto:{}",dto.toString());
        dto.setCancelTime(LocalDateTime.now());
        orderMapper.cancelOrder(dto);
    }

    @Override
    public void deliveryOrder(Long id) {
        orderMapper.delivery(id);
    }

    @Override
    public void competeOrder(Long id) {
        orderMapper.compeleteOrder(id);
    }


    public void paySuccess(String orderId) {
        Orders orders = orderMapper.getBy(orderId, BaseContext.getCurrentId());

        Orders order = Orders.builder()
                .id(orders.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .estimatedDeliveryTime(LocalDateTime.now().plusHours(1))
                .build();
        orderMapper.update(order);
    }


    private void checkAddress(String address) {
        Map<String,String> map = new HashMap<>();
        map.put("address", shopAddress);
        map.put("ak",ak);
        map.put("output","json");
        String shopRes = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3",map);
        log.info("shopRes:{}",shopRes);

        JSONObject jsonObject = JSONObject.parseObject(shopRes);
        if(!jsonObject.getString("status").equals("0")){
            throw new OrderBusinessException("店铺地址解析失败");
        }
        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
        String lng = location.getString("lng");
        String lat = location.getString("lat");

        String shopLag = lat+","+lng;

        map.put("address",address);
        String userRes = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3",map);
        log.info("userRes:{}",userRes);

        JSONObject userJsonObject = JSONObject.parseObject(userRes);
        if(!userJsonObject.getString("status").equals("0")){
            throw new OrderBusinessException("用户地址解析失败");
        }
        JSONObject userLocation = userJsonObject.getJSONObject("result").getJSONObject("location");
        String userLng = userLocation.getString("lng");
        String userLat = userLocation.getString("lat");
        String userLag = userLat+","+userLng;
        map.put("origin",shopLag);
        map.put("destination",userLag);
        map.put("steps_info","0");

        String res = HttpClientUtil.doGet("https://api.map.baidu.com/directionlite/v1/driving",map);
        log.info("res:{}",res);
        jsonObject = JSONObject.parseObject(res);
        if(!jsonObject.getString("status").equals("0")){
            throw new OrderBusinessException("路线规划失败");
        }
        //数据解析
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray jsonArray = (JSONArray) result.get("routes");
        Integer distance = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");

        log.info("distance:{}",distance);
        if(distance > 5000){
            //配送距离超过5000米
            throw new OrderBusinessException("超出配送范围");
        }


//        Map map = new HashMap();
//        map.put("address",shopAddress);
//        map.put("output","json");
//        map.put("ak",ak);
//
//        //获取店铺的经纬度坐标
//        String shopCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);
//
//        JSONObject jsonObject = JSON.parseObject(shopCoordinate);
//        if(!jsonObject.getString("status").equals("0")){
//            throw new OrderBusinessException("店铺地址解析失败");
//        }
//
//        //数据解析
//        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
//        String lat = location.getString("lat");
//        String lng = location.getString("lng");
//        //店铺经纬度坐标
//        String shopLngLat = lat + "," + lng;
//
//        map.put("address",address);
//        //获取用户收货地址的经纬度坐标
//        String userCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);
//
//        jsonObject = JSON.parseObject(userCoordinate);
//        if(!jsonObject.getString("status").equals("0")){
//            throw new OrderBusinessException("收货地址解析失败");
//        }
//
//        //数据解析
//        location = jsonObject.getJSONObject("result").getJSONObject("location");
//        lat = location.getString("lat");
//        lng = location.getString("lng");
//        //用户收货地址经纬度坐标
//        String userLngLat = lat + "," + lng;
//
//        map.put("origin",shopLngLat);
//        map.put("destination",userLngLat);
//        map.put("steps_info","0");
//
//        //路线规划
//        String json = HttpClientUtil.doGet("https://api.map.baidu.com/directionlite/v1/driving", map);
//
//        jsonObject = JSON.parseObject(json);
//        if(!jsonObject.getString("status").equals("0")){
//            throw new OrderBusinessException("配送路线规划失败");
//        }
//
//        //数据解析
//        JSONObject result = jsonObject.getJSONObject("result");
//        JSONArray jsonArray = (JSONArray) result.get("routes");
//        Integer distance = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");
//
//        if(distance > 5000){
//            //配送距离超过5000米
//            throw new OrderBusinessException("超出配送范围");
//        }
    }
}
