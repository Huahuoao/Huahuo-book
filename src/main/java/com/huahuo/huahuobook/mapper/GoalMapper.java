package com.huahuo.huahuobook.mapper;

import com.huahuo.huahuobook.pojo.Goal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【goal】的数据库操作Mapper
* @createDate 2023-03-09 23:41:13
* @Entity com.huahuo.huahuobook.pojo.Goal
*/
@Mapper
public interface GoalMapper extends BaseMapper<Goal> {

}




