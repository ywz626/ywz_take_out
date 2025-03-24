package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 于汶泽
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public List<ShoppingCart> listCart() {
        return shoppingCartMapper.listCart(BaseContext.getCurrentId());
    }

    @Override
    public void saveCart(ShoppingCartDTO dto) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(dto, shoppingCart);

        shoppingCart.setUserId(BaseContext.getCurrentId());
        //判断购物车是否已经有该商品
        ShoppingCart cart = shoppingCartMapper.selectBy(shoppingCart);

        if(cart==null){
            //购物车中没有该商品
            //判断是菜品还是套餐
            if(shoppingCart.getDishId()!=null){
                DishVO dishVO = dishMapper.getById(shoppingCart.getDishId());
                shoppingCart.setImage(dishVO.getImage());
                shoppingCart.setName(dishVO.getName());
                shoppingCart.setAmount(dishVO.getPrice());
            }else {
                SetmealVO setmealVO = setmealMapper.getById(shoppingCart.getSetmealId());
                shoppingCart.setImage(setmealVO.getImage());
                shoppingCart.setName(setmealVO.getName());
                shoppingCart.setAmount(setmealVO.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.saveCart(shoppingCart);
        }else {
            //购物车中有该商品
            //判断是菜品还是套餐
            if(cart.getDishId()!=null){
                DishVO dishVO = dishMapper.getById(cart.getDishId());
                cart.setNumber(cart.getNumber()+1);
            }
            else {
                SetmealVO setmealVO = setmealMapper.getById(cart.getSetmealId());
                cart.setNumber(cart.getNumber()+1);
            }
            shoppingCartMapper.updateCart(cart);
        }
    }

    @Override
    public void cleanCart() {
        shoppingCartMapper.cleanCart(BaseContext.getCurrentId());
    }

    @Override
    public void subCart(ShoppingCartDTO dto) {
        ShoppingCart c = new ShoppingCart();
        BeanUtils.copyProperties(dto, c);
        ShoppingCart cart = shoppingCartMapper.selectBy(c);
        Integer number = cart.getNumber();
        if(number>1){
            cart.setNumber(number-1);
            shoppingCartMapper.updateCart(cart);
        }else if (number==1){
            shoppingCartMapper.delete(cart);
        }else {
            throw new RuntimeException();
        }
    }
}
