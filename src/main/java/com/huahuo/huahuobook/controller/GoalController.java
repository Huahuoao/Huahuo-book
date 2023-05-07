package com.huahuo.huahuobook.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.common.aop.LogAnnotation;
import com.huahuo.huahuobook.dto.CycleDto;
import com.huahuo.huahuobook.pojo.Goal;
import com.huahuo.huahuobook.service.GoalService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.List;

/**
 * @作者 花火
 * @创建日期 2023/3/13 8:23
 */
@RequestMapping("/goal")
@RestController
public class GoalController {
    @Autowired
    GoalService goalService;

    //查询初始目标
    @GetMapping("/get/first/goal/{type}")
    @LogAnnotation(module = "goal", operator = "获取初始目标")
    public ResponseResult getRecords(@PathVariable Integer type) {
        return goalService.getRecords(type);
    }
//以下接口文档没写
    //新建目标
    @PostMapping("/new")
    @LogAnnotation(module = "goal", operator = "新建目标")
    public ResponseResult newRecord(@RequestBody Goal goal) {
        goal.setCreateTime(DateUtil.today());
        goal.setEndTime("2099-1-1");
        goalService.save(goal);
        Integer id = goal.getId();
        return ResponseResult.okResult(id);
    }

    //定期存入
    @PostMapping("/schedule/add/money")
    @LogAnnotation(module = "goal", operator = "定期存入")
    public ResponseResult scheduleAddMoney(@RequestBody CycleDto dto) {
        return goalService.scheduleAddMoney(dto);
    }

    //手动存入
    @PostMapping("/normal/add/money")
    @LogAnnotation(module = "goal", operator = "手动存入")
    public ResponseResult normalAddMoney(@RequestBody CycleDto dto) {

        return goalService.normalAddMoney(dto);
    }


    @GetMapping("/list/goal/{id}")
    public ResponseResult listGoalById(@PathVariable("id") Integer id){
        LambdaQueryWrapper<Goal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Goal::getUserId,id);
        List<Goal> list = goalService.list(queryWrapper);
        return ResponseResult.okResult(list);
    }
}
