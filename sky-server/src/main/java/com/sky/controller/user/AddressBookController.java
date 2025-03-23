package com.sky.controller.user;

import com.sky.anno.AutoFill;
import com.sky.dto.AddressBookDefaultDTO;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 于汶泽
 */
@RequestMapping("/user/addressBook")
@RestController
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询所有地址
     * @return
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list(){
        return Result.success(addressBookService.list());
    }

    /**
     * 新增地址
     * @param address
     * @return
     */
    @PostMapping
    public Result saveAddress(@RequestBody AddressBook address){
        addressBookService.saveAddress(address);
        return Result.success();
    }

    /**
     * 修改地址时回显
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Result<AddressBook> getAddressById(@PathVariable Long id){
        return Result.success(addressBookService.getById(id));
    }

    /**
     * 修改地址
     * @param address
     * @return
     */
    @PutMapping
    public Result updateAddress(@RequestBody AddressBook address){
        addressBookService.updateAddress(address);
        return Result.success();
    }

    /**
     * 修改默认地址
     * @param status
     * @return
     */
    @PutMapping("/default")
    public Result defaultStatus(@RequestBody AddressBookDefaultDTO status){
        addressBookService.updateStatus(status);
        return Result.success();
    }

    /**
     * 删除地址
     * @param id
     * @return
     */
    @DeleteMapping
    public Result deleteAddress(Integer id){
        addressBookService.delete(id);
        return Result.success();
    }

    @GetMapping("/default")
    public Result<AddressBook> getDefaultAddressBook(){
        return Result.success(addressBookService.getDefault());
    }
}
