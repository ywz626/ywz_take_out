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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.basic.BasicListUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author 于汶泽
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private RedisTemplate redisTemplate;

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
        redisTemplate.delete("dish_"+doDish.getCategoryId());
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

        DishVO dishVO = dishMapper.getById(id);
        Long categoryId = dishVO.getCategoryId();
        redisTemplate.delete("dish_" + categoryId);
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
        Set key = redisTemplate.keys("dish_*");
        redisTemplate.delete(key);
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        List<Long> categoryIdById = dishMapper.getCategoryIdById(ids);
        ArrayList<String> strings = new ArrayList<>();
        for (Long categoryId : categoryIdById) {
            strings.add("dish_" + categoryId);
        }
        redisTemplate.delete(strings);
        dishMapper.deleteDish(ids);
        dishMapper.deleteFlavorById(ids);
    }

    @Override
    public List<Dish> listAdmin(Long categoryId) {
        return dishMapper.listAdmin(categoryId);
    }


    /**
     * 用户端查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<DishVO> listUser(Long categoryId) {
        String key = "dish_" + categoryId;
        List<DishVO> dishVo =(List<DishVO>) redisTemplate.opsForValue().get(key);
        if(dishVo!=null&&dishVo.size()>0){
            return dishVo;
        }

        List<DishVO> dishVOS = dishMapper.listUser(categoryId);
        for(DishVO dvo:dishVOS){
            List<DishFlavor> flavorsById = dishMapper.getFlavorsById(dvo.getId());
            dvo.setFlavors(flavorsById);
        }
        redisTemplate.opsForValue().set(key,dishVOS);
        return dishVOS;
    }
}
