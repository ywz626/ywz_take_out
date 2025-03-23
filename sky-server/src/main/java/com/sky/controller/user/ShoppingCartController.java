package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 于汶泽
 */
@RequestMapping("/user/shoppingCart")
@RestController
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 查看购物车列表
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {
        return Result.success(shoppingCartService.listCart());
    }

    /**
     * 在购物车中添加商品
     * @param dto
     * @return
     */
    @PostMapping("/add")
    public Result saveCart(@RequestBody ShoppingCartDTO dto){
        shoppingCartService.saveCart(dto);
        return Result.success();
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public Result clean(){
        shoppingCartService.cleanCart();
        return Result.success();
    }

    /**
     * 商品数量减一
     * @param dto
     * @return
     */
    @PostMapping("/sub")
    public Result subCart(@RequestBody ShoppingCartDTO dto){
        shoppingCartService.subCart(dto);
        return Result.success();
    }
}
