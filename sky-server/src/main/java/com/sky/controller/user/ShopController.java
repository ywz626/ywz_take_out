package com.sky.controller.user;

import com.sky.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author 于汶泽
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
public class ShopController {

    private final static String key = "shop_status";


    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 设置店铺营业状态
     * @return
     */
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        return Result.success((Integer)redisTemplate.opsForValue().get(key));
    }
}
