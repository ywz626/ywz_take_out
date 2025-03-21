package com.sky.mapper;

import com.sky.anno.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @AutoFill(OperationType.INSERT)
    void saveEmployee(Employee emp);

    List<Employee> pageEmployee(EmployeePageQueryDTO emp);

    @AutoFill(OperationType.UPDATE)
    void update(Employee emp);

    @Select("select * from employee where id=#{id}")
    Employee getById(Long id);
}
