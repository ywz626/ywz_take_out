package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 于汶泽
 */
@Mapper
public interface AddressBookMapper {
    @Select("select * from address_book")
    List<AddressBook> list();

    @Insert("insert into address_book values (null,#{userId},#{consignee},#{sex},#{phone},#{provinceCode},#{provinceName},#{cityCode},#{cityName},#{districtCode},#{districtName},#{detail},#{label},#{isDefault})")
    void saveAddress(AddressBook address);

    @Select("select * from address_book where id=#{id}")
    AddressBook getById(Long id);

    void updateAddress(AddressBook address);

    @Delete("delete from address_book where id=#{id}")
    void delete(Integer id);

    @Select("select * from address_book where is_default=1")
    AddressBook getDefault();
}
