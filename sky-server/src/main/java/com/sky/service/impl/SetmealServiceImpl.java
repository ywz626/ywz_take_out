package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author 于汶泽
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增套餐
     * @param setmeal
     */
    @CacheEvict(value = "setmeal",key = "#setmeal.categoryId")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSetmeal(SetmealDTO setmeal) {
        Setmeal setm = new Setmeal();
        BeanUtils.copyProperties(setmeal, setm);

        setmealMapper.saveSetmeal(setm);
        Long id = setm.getId();
        List<SetmealDish> setmealDishes = setmeal.getSetmealDishes();
        if (setmealDishes != null&&setmealDishes.size()>0) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(id);
            }
            setmealMapper.saveSetmealDish(setmealDishes);
        }
//        redisTemplate.delete("setmeal_"+setmeal.getCategoryId());
    }

    @Override
    public PageResult pageSetmeal(SetmealPageQueryDTO setmeal) {
        PageHelper.startPage(setmeal.getPage(), setmeal.getPageSize());
        Page page = (Page) setmealMapper.pageSetmeal(setmeal);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @CacheEvict(value = "setmeal", allEntries = true)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
//        List<Long> categorysIdById = setmealMapper.getCategorysIdById(ids);
//        List<String> s = new ArrayList<>();
//        for (Long id : categorysIdById) {
//            s.add("setmeal_" + id);
//        }
//        redisTemplate.delete(s);
        setmealMapper.deleteSetmeal(ids);
        setmealMapper.deleteSetmealDish(ids);
    }

    @Override
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO = setmealMapper.getById(id);
        List<SetmealDish> dishById = setmealMapper.getDishById(id);
        setmealVO.setSetmealDishes(dishById);
        return setmealVO;
    }

    @CacheEvict(value = "setmeal", allEntries = true)
    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.updateSetmeal(setmeal);
        List<Long> ids = new ArrayList<>();
        ids.add(setmeal.getId());
        setmealMapper.deleteSetmealDish(ids);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null&&setmealDishes.size()>0) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmeal.getId());
            }
            setmealMapper.saveSetmealDish(setmealDishes);
        }
//        Set key = redisTemplate.keys("setmeal_*");
//        redisTemplate.delete(key);
    }

    @CacheEvict(value = "setmeal", allEntries = true)
    @Override
    public void updateStatus(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
//        Long categoryIdById = setmealMapper.getCategoryIdById(id);
        setmealMapper.updateSetmeal(setmeal);
//        redisTemplate.delete("setmeal_"+setmeal.getCategoryId());
    }

    @Override
    public List<Setmeal> listSetmeal(Long id) {
//        List<Setmeal> list =(List<Setmeal>) redisTemplate.opsForValue().get("setmeal_" + id);
//        if(list!=null&&list.size()>0) {
//            return list;
//        }
        List<Setmeal>list = setmealMapper.listSetmeal(id);
//        redisTemplate.opsForValue().set("setmeal_" + id, list);
        return list;
    }

    @Override
    public List<SetmealDish> listDish(Long id) {
        return setmealMapper.listDish(id);
    }
}
