package com.sky.service;

import com.sky.dto.AddressBookDefaultDTO;
import com.sky.entity.AddressBook;
import com.sky.result.Result;

import java.util.List;

/**
 * @author 于汶泽
 */
public interface AddressBookService {
    List<AddressBook> list();

    void saveAddress(AddressBook address);

    AddressBook getById(Long id);

    void updateAddress(AddressBook address);

    void updateStatus(AddressBookDefaultDTO status);

    void delete(Integer id);

    AddressBook getDefault();
}
