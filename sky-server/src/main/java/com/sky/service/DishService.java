package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @author 于汶泽
 */

public interface DishService {
    void saveDish(DishDTO dish);

    PageResult pageDish(DishPageQueryDTO dto);

    void updateStatus(Integer status, Long id);

    DishVO getById(Long id);

    void update(DishDTO dishDTO);

    void delete(List<Long> ids);

    List<Dish> list(Long categoryId);
}
