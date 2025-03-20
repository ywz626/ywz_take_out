package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 于汶泽
 */
@Mapper
public interface DishMapper {

    @Select("select count(*) from dish where category_id=#{id}")
    Integer getCountByCategoryId(Integer id);
}
