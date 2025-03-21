package com.sky.aspect;

import com.sky.anno.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author 于汶泽
 */
@Component
@Aspect
public class AutoFillAspect {

    @Before("@annotation(com.sky.anno.AutoFill)")
    public void autoFill(JoinPoint joinPoint) {
        MethodSignature methodSig = (MethodSignature) joinPoint.getSignature();
        Method method = methodSig.getMethod();
        AutoFill autoFill = method.getAnnotation(AutoFill.class);
        OperationType value = autoFill.value();

        Object[] args = joinPoint.getArgs();
        if (!(args != null && args.length > 0)) {
            return;
        }
        Object target = args[0];
        try {
            if(value == OperationType.INSERT){
                Method setCreatTime = target.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                setCreatTime.invoke(target,LocalDateTime.now());
                Method setUpdateTime = target.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                setUpdateTime.invoke(target,LocalDateTime.now());
                Method setCreateUser = target.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                setCreateUser.invoke(target, BaseContext.getCurrentId());
                Method setUpdateUser = target.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateUser.invoke(target, BaseContext.getCurrentId());
            }else if (value == OperationType.UPDATE){
                Method setCreateUser = target.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, Long.class);
                setCreateUser.invoke(target, BaseContext.getCurrentId());
                Method setUpdateUser = target.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, Long.class);
                setUpdateUser.invoke(target, BaseContext.getCurrentId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
