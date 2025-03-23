package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.AddressBookDefaultDTO;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 于汶泽
 */
@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;
    @Override
    public List<AddressBook> list() {
        return addressBookMapper.list();
    }

    @Override
    public void saveAddress(AddressBook address) {
        address.setUserId(BaseContext.getCurrentId());
        address.setIsDefault(0);
        addressBookMapper.saveAddress(address);
    }

    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }

    @Override
    public void updateAddress(AddressBook address) {
        addressBookMapper.updateAddress(address);
    }

    @Override
    public void updateStatus(AddressBookDefaultDTO status) {
        AddressBook addressBook = addressBookMapper.getById(status.getId());
        Integer isDefault = addressBook.getIsDefault();
        addressBook.setIsDefault(isDefault==1?0:1);
        addressBookMapper.updateAddress(addressBook);
    }

    @Override
    public void delete(Integer id) {
        addressBookMapper.delete(id);
    }

    @Override
    public AddressBook getDefault() {
        return addressBookMapper.getDefault();
    }
}
