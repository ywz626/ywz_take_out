package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    @PutMapping("/editPassword")
    public Result editPassword(@RequestBody PasswordEditDTO pwdPram) {
        employeeService.updatePwd(pwdPram);
        return Result.success();
    }

    /**
     * 修改员工信息
     * @param emp
     * @return
     */
    @PutMapping
    public Result update(@RequestBody EmployeeDTO emp) {
        employeeService.update(emp);
        return Result.success();
    }

    /**
     * 修改员工信息的时候回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> getEmployeeById(@PathVariable Long id) {
        return Result.success(employeeService.getById(id));
    }

    /**
     * 启用禁用员工功能
     * @param status
     * @param id
     * @return
     */
    @PostMapping("status/{status}")
    public Result updateStatus(@PathVariable("status") Integer status,Long id)
    {
        employeeService.updateStatus(status,id);
        return Result.success();
    }

    /**
     * 分页查询
     * @param emp
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> employeePage(EmployeePageQueryDTO emp){
        log.info("分页查询的信息：{}",emp);
        return Result.success(employeeService.pageEmployee(emp));
    }

    /**
     * 新增员工
     * @param emp
     * @return
     */
    @PostMapping("employee")
    public Result<String> saveEmployee(@RequestBody Employee emp) {
        employeeService.saveEmployee(emp);
        return Result.success();
    }

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

}
