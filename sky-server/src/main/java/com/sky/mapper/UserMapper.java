package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * @author 于汶泽
 */
@Mapper
public interface UserMapper {

    @Insert("insert user(name,openid,create_time) values (#{name},#{openid},#{createTime})")
    void insertUser(User user);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Select("select * from user where openid=#{openid}")
    User getUser(String openid);
}
