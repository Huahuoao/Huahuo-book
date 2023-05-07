package com.huahuo.huahuobook.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.huahuo.huahuobook.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-01-23 23:54:55
* @Entity com.huahuo.huahuobook.pojo.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where phone = #{phone}")
    User selectByPhoneNumber(@Param("phone") String phone);
}




