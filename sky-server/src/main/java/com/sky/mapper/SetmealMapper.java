package com.sky.mapper;

import com.sky.anno.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 于汶泽
 */
@Mapper
public interface SetmealMapper {


    @AutoFill(OperationType.INSERT)
    void saveSetmeal(Setmeal setmeal);


    void saveSetmealDish(List<SetmealDish> setmealDishes);

    List<SetmealVO> pageSetmeal(SetmealPageQueryDTO setmeal);

    void deleteSetmeal(List<Long> ids);

    void deleteSetmealDish(List<Long> ids);

    SetmealVO getById(Long id);

    List<SetmealDish> getDishById(Long id);

    @AutoFill(OperationType.UPDATE)
    void updateSetmeal(Setmeal setmeal);


    List<Setmeal> listSetmeal(Long id);

    List<SetmealDish> listDish(Long id);

    Long getCategoryIdById(Long id);

    List<Long> getCategorysIdById(List<Long> ids);
}
