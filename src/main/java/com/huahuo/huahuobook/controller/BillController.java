package com.huahuo.huahuobook.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.common.aop.LogAnnotation;
import com.huahuo.huahuobook.dto.BillDto;
import com.huahuo.huahuobook.dto.BillPageDto;
import com.huahuo.huahuobook.pojo.Bill;
import com.huahuo.huahuobook.pojo.Img;
import com.huahuo.huahuobook.service.BillService;
import com.huahuo.huahuobook.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @作者 花火
 * @创建日期 2023/1/25 17:43
 */
@RestController
@RequestMapping("/bill")
public class BillController {
    @Autowired
    BillService billService;
    @Autowired
    ImgService imgService;
    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/add")
    @LogAnnotation(module = "bill", operator = "新建账单")
    @CacheEvict(value = "bill", allEntries = true)
    public ResponseResult<String> add(@RequestBody Bill bill) {
        return billService.add(bill);
    }

    @LogAnnotation(module = "bill", operator = "查询账单")
    @PostMapping("/list/book")
    @Cacheable(value = "bill",key = "#root.args[0]+'_'+#root.args[0].page+'_'+#root.args[0].size")
    public ResponseResult listBillByBook(@RequestBody BillPageDto billPageDto) {
        System.out.println("缓存没命中！");
        return billService.listBillByBook(billPageDto);
    }

    @GetMapping("/get/img/{id}")
    @LogAnnotation(module = "bill", operator = "获取账单中的图片")
    public ResponseResult getImgUrls(@PathVariable Integer id) {
        LambdaQueryWrapper<Img> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Img::getBillId, id);
        List<Img> list = imgService.list(queryWrapper);
        return ResponseResult.okResult(list);

    }
@GetMapping("/remove/img/{id}")
@CacheEvict(value = "bill", allEntries = true)
public ResponseResult removeImg(@PathVariable("id") Integer id){
    imgService.removeById(id);
        return ResponseResult.okResult("删除成功");
}


    @PostMapping("/update")
    @CacheEvict(value = "bill", allEntries = true)
    @LogAnnotation(module = "bill", operator = "修改账单")
    public ResponseResult updateBill(@RequestBody Bill bill) {
            return    billService.updateBill(bill);
    }
}
