package com.sky.mapper;

import com.sky.anno.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 于汶泽
 */
@Mapper
public interface DishMapper {

    /**
     * 表单回显数据
     * @param id
     * @return
     */
    @Select("select count(*) from dish where category_id=#{id}")
    Integer getCountByCategoryId(Integer id);


    /**
     * 新增菜品
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void saveDish(Dish dish);

    /**
     * 新增菜品的口味
     * @param dishFlavors
     */
    void saveDishFlavor(List<DishFlavor> dishFlavors);

    /**
     * 分页查询
     * @param dto
     * @return
     */

    List<DishVO> pageDish(DishPageQueryDTO dto);


    /**
     * 修改菜品信息
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 修改菜品信息时表单回显数据
     * @param id
     * @return
     */
    DishVO getById(Long id);

    /**
     * 获取与菜品id对应的口味集合
     * @param id
     * @return
     */
    List<DishFlavor> getFlavorsById(Long id);

    /**
     * 批量删除菜品id对应的口味
     * @param ids
     */
    //@Delete("delete from dish_flavor where dish_id=#{id}")
    void deleteFlavorById(List<Long> ids);


    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteDish(List<Long> ids);

    @Select("select * from dish where category_id=#{categoryId}")
    List<Dish> listAdmin(Long categoryId);

    List<DishVO> listUser(Long categoryId);

    List<Long> getCategoryIdById(List<Long> ids);
}
