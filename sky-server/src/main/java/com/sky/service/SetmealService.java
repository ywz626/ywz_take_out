package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * @author 于汶泽
 */
public interface SetmealService {

    void saveSetmeal(SetmealDTO setmeal);

    PageResult pageSetmeal(SetmealPageQueryDTO setmeal);

    void delete(List<Long> ids);

    SetmealVO getById(Long id);

    void update(SetmealDTO setmealDTO);

    void updateStatus(Integer status, Long id);

    List<Setmeal> listSetmeal(Long id);

    List<SetmealDish> listDish(Long id);
}
