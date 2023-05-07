package com.huahuo.huahuobook.mapper;

import com.huahuo.huahuobook.pojo.Relation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Administrator
* @description 针对表【relation】的数据库操作Mapper
* @createDate 2023-01-27 17:09:46
* @Entity com.huahuo.huahuobook.pojo.Relation
*/
@Mapper
public interface RelationMapper extends BaseMapper<Relation> {

    @Select("select * from relation where user_id = #{userId}")
    List<Relation> listBookIdByUserId(@Param("userId") Integer userId);

    }




