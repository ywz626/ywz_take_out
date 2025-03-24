package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 于汶泽
 */
@Mapper
public interface ShoppingCartMapper {

    @Select("select * from shopping_cart where user_id=#{userId}")
    List<ShoppingCart> listCart(Long userId);

    ShoppingCart selectBy(ShoppingCart shoppingCart);

    @Insert("insert into shopping_cart values(null,#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")
    void saveCart(ShoppingCart shoppingCart);

    @Update("update shopping_cart set number=#{number} where id=#{id}")
    void updateCart(ShoppingCart cart);

    @Delete("delete from shopping_cart where user_id=#{currentId}")
    void cleanCart(Long currentId);

    @Delete("delete from shopping_cart where id=#{id}")
    void delete(ShoppingCart cart);
}
