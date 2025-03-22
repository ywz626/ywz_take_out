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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 于汶泽
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增套餐
     * @param setmeal
     */
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
    }

    @Override
    public PageResult pageSetmeal(SetmealPageQueryDTO setmeal) {
        PageHelper.startPage(setmeal.getPage(), setmeal.getPageSize());
        Page page = (Page) setmealMapper.pageSetmeal(setmeal);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
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
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setmealMapper.updateSetmeal(setmeal);
    }

    @Override
    public List<Setmeal> listSetmeal(Long id) {
        return setmealMapper.listSetmeal(id);
    }

    @Override
    public List<SetmealDish> listDish(Long id) {
        return setmealMapper.listDish(id);
    }
}
