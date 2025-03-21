package com.sky.mapper;

import com.sky.anno.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 于汶泽
 */
@Mapper
public interface CategoryMapper {
    List<Category> CategoryPage(CategoryPageQueryDTO cDto);

    @AutoFill(OperationType.INSERT)
    void saveCategory(Category cate);

    @AutoFill(OperationType.UPDATE)
    void updateCategory(Category category);

    @Delete("delete from category where id=#{id}")
    void deleteCategory(Integer id);

    @Select("select * from category where type=#{type} order by sort")
    List<Category> getList(Integer type);
}
