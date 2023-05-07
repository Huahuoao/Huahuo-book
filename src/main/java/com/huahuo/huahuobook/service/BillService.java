package com.huahuo.huahuobook.service;

import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.dto.BillDto;
import com.huahuo.huahuobook.dto.BillPageDto;
import com.huahuo.huahuobook.pojo.Bill;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

/**
* @author Administrator
* @description 针对表【bill】的数据库操作Service
* @createDate 2023-01-25 17:59:33
*/
public interface BillService extends IService<Bill> {
    public ResponseResult<String> add(Bill bill);
    public ResponseResult listBillByBook(@RequestBody BillPageDto billPageDto);
    public ResponseResult<String> add2(Bill bill);
    ResponseResult updateBill(Bill bill);
}
