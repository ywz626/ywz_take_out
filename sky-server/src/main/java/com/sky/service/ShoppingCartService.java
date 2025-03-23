package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @author 于汶泽
 */
public interface ShoppingCartService {
    List<ShoppingCart> listCart();

    void saveCart(ShoppingCartDTO dto);

    void cleanCart();

    void subCart(ShoppingCartDTO dto);
}
