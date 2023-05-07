package com.huahuo.huahuobook.mapper;

import com.huahuo.huahuobook.pojo.Bill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【bill】的数据库操作Mapper
* @createDate 2023-01-25 17:59:33
* @Entity com.huahuo.huahuobook.pojo.Bill
*/
@Mapper
public interface BillMapper extends BaseMapper<Bill> {

}




