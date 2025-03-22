package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author 于汶泽
 */
public interface UserService {
    User login(UserLoginDTO dto);
}
