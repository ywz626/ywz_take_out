package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.anno.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 于汶泽
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishService dishService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveDish(DishDTO dish) {
        Dish doDish = new Dish();
        BeanUtils.copyProperties(dish, doDish);
        dishMapper.saveDish(doDish);
        log.info("doDish:{}", doDish);
        Long id = doDish.getId();
        log.info("id: {}",id);
        List<DishFlavor> flavors = dish.getFlavors();
        if(flavors!=null&&flavors.size()>0){
            for(DishFlavor df:flavors){
                df.setDishId(id);
            }
            dishMapper.saveDishFlavor(flavors);
        }
    }

    @Override
    public PageResult pageDish(DishPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(),dto.getPageSize());
        Page page = (Page) dishMapper.pageDish(dto);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.update(dish);
    }

    @Override
    public DishVO getById(Long id) {
        DishVO dishVO = dishMapper.getById(id);
        dishVO.setFlavors(dishMapper.getFlavorsById(id));
        return dishVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);
        List<Long> ids = new ArrayList<Long>();
        ids.add(dishDTO.getId());
        dishMapper.deleteFlavorById(ids);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null&&flavors.size()>0){
            for(DishFlavor df:flavors){
                df.setDishId(dish.getId());
            }
            dishMapper.saveDishFlavor(flavors);
        }
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        dishMapper.deleteDish(ids);
        dishMapper.deleteFlavorById(ids);
    }

    @Override
    public List<Dish> list(Long categoryId) {
        return dishMapper.list(categoryId);
    }


}
