package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @author 于汶泽
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(UserLoginDTO dto) {
        String code = dto.getCode();
        if(code == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("grant_type","authorization_code");
        map.put("js_code",code);

        String res = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/jscode2session", map);
        log.info("res : {}", res);
        JSONObject jsonRes = JSON.parseObject(res);
        String openId = jsonRes.getString("openid");
        if(openId == null) {
            throw new LoginFailedException(MessageConstant.USER_NOT_LOGIN);
        }

        User user = userMapper.getUser(openId);
        if(user == null) {
            user = new User();
            user.setOpenid(openId);
            user.setCreateTime(LocalDateTime.now());
            user.setName(openId.substring(0,5));
            userMapper.insertUser(user);
        }
        return user;
    }
}
