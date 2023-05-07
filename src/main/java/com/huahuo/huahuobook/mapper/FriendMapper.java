package com.huahuo.huahuobook.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.huahuo.huahuobook.pojo.Friend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.List;

/**
* @author Administrator
* @description 针对表【friend】的数据库操作Mapper
* @createDate 2023-01-28 15:59:36
* @Entity com.huahuo.huahuobook.pojo.Friend
*/
@Mapper
public interface FriendMapper extends BaseMapper<Friend> {

    @Select(" select * from friend where user_id = #{userId}")
    List<Friend> listByUserId(@Param("userId") Integer userId);
}




