package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 于汶泽
 */
@Mapper
public interface OrderDetailMapper {
    void insertBatch(List<OrderDetail> orderDetail);

    @Select("select * from order_detail where order_id=#{id}")
    List<String> adminGetDishesByOrderId(Long id);
}
