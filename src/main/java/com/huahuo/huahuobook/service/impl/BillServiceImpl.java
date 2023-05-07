package com.huahuo.huahuobook.service.impl;

import cn.hutool.core.lang.hash.Hash;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huahuo.huahuobook.common.PageResponseResult;
import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.dto.BillPageDto;
import com.huahuo.huahuobook.pojo.Bill;
import com.huahuo.huahuobook.pojo.Book;
import com.huahuo.huahuobook.service.BillService;
import com.huahuo.huahuobook.mapper.BillMapper;
import com.huahuo.huahuobook.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author Administrator
 * @description 针对表【bill】的数据库操作Service实现
 * @createDate 2023-01-25 17:59:33
 */
@Service
@Slf4j
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill>
        implements BillService {
    @Autowired
    private BookService bookService;

    @Override
    public ResponseResult<String> add(Bill bill) {
        Book book = bookService.getById(bill.getBookId());
        if(bill.getTypeOne()==1)
        book.setTotalExpense(book.getTotalExpense()+bill.getNum());
        else if(bill.getTypeOne() ==2)
            book.setTotalIncome(book.getTotalIncome()+bill.getNum());
        if (book.getBudget() != null) {
            if (bill.getTypeOne() == 1)
                book.setBalance(book.getBalance() - bill.getNum());
            else {
                book.setBalance(book.getBalance() + bill.getNum());
            }
        }
        bookService.updateById(book);
        bill.setTypeThree(1);
        save(bill);
        HashMap map = new HashMap(2);
        map.put("bill_id",bill.getId());
        map.put("msg","添加账单成功");
        map.put("userId",bill.getUserId());
        return ResponseResult.okResult(map);
    }
    @Override
    public ResponseResult<String> add2(Bill bill) {
        log.info(bill.toString());
        Book book = bookService.getById(bill.getBookId());
        if (book.getBudget() != null) {
            if (bill.getTypeOne() == 1)
                book.setBalance(book.getBalance() - bill.getNum());
            else {
                book.setBalance(book.getBalance() + bill.getNum());
            }
        }
        bookService.updateById(book);
        bill.setTypeThree(2);
        save(bill);
        HashMap map = new HashMap(2);
        map.put("bill_id",bill.getId());
        map.put("msg","添加账单成功");
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult listBillByBook(BillPageDto billPageDto) {

        IPage page = new Page(billPageDto.getPage(), billPageDto.getSize());
        LambdaQueryWrapper<Bill> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Bill::getBookId, billPageDto.getBookId());
        lambdaQueryWrapper.orderByDesc(Bill::getCreateTime);
        if (StringUtils.isNotBlank(billPageDto.getTypeTwo())) {
            lambdaQueryWrapper.eq(Bill::getTypeTwo, billPageDto.getTypeTwo());
        }
        if (billPageDto.getTypeOne() != 0) {
            lambdaQueryWrapper.eq(Bill::getTypeOne, billPageDto.getTypeOne());
        }
        // 关键词
        if (StringUtils.isNotBlank(billPageDto.getKeyword())) {
            lambdaQueryWrapper.like(Bill::getText, billPageDto.getKeyword());
        }
        // 关键词
        if (billPageDto.getPayWay()!=null) {
            lambdaQueryWrapper.like(Bill::getPayWay, billPageDto.getPayWay());
        }

        //俩时间之间
        if (StringUtils.isNotBlank(billPageDto.getBeginTime())) {
            lambdaQueryWrapper.between(Bill::getCreateTime, billPageDto.getBeginTime(), billPageDto.getEndTime());
        }
        //
        if (billPageDto.getIsCollect() == 1) {
            lambdaQueryWrapper.eq(Bill::getIsCollect, 1);
        }
        IPage pageResult = page(page, lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(billPageDto.getPage(), billPageDto.getSize(), (int) page.getTotal());
        responseResult.setData(pageResult.getRecords());
        return responseResult;
    }

    @Override
    public ResponseResult updateBill(Bill bill) {
        Bill realBill = getById(bill.getId());
        Book book = bookService.getById(bill.getBookId());
        Double beforeNum = realBill.getNum();
        //修改金额
        if (bill.getNum() != realBill.getNum()) {
            realBill.setNum(bill.getNum());
            if (bill.getTypeOne() == 1) {
                book.setBalance(book.getBalance() + beforeNum);
                book.setBalance(book.getBalance() - bill.getNum());
            } else if (bill.getTypeOne() == 2) {
                book.setBalance(book.getBalance() - beforeNum);
                book.setBalance(book.getBalance() + bill.getNum());
            }
        }
        //修改备注
        realBill.setText(bill.getText());
        //是否收藏
        realBill.setIsCollect(1);
        //改类型
        realBill.setTypeTwo(bill.getTypeTwo());
        //改支付方式
        realBill.setPayWay(bill.getPayWay());
        //保存一下
        bookService.updateById(book);
        updateById(realBill);
        return ResponseResult.okResult("修改成功");
    }
}



