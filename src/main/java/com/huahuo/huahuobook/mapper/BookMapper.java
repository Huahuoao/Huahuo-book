package com.huahuo.huahuobook.mapper;

import com.huahuo.huahuobook.pojo.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Administrator
* @description 针对表【book】的数据库操作Mapper
* @createDate 2023-01-27 17:35:12
* @Entity com.huahuo.huahuobook.pojo.Book
*/
@Mapper
public interface BookMapper extends BaseMapper<Book> {

    Book selectByTempCode(@Param("tempCode") String tempCode);
}




