package com.huahuo.huahuobook.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.dto.CycleDto;
import com.huahuo.huahuobook.pojo.Bill;
import com.huahuo.huahuobook.pojo.Goal;
import com.huahuo.huahuobook.pojo.User;
import com.huahuo.huahuobook.service.BillService;
import com.huahuo.huahuobook.service.GoalService;
import com.huahuo.huahuobook.mapper.GoalMapper;
import com.huahuo.huahuobook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @description 针对表【goal】的数据库操作Service实现
 * @createDate 2023-03-09 23:41:13
 */
@Service
public class GoalServiceImpl extends ServiceImpl<GoalMapper, Goal>
        implements GoalService {

    @Autowired
    UserService userService;
    @Autowired
    BillService billService;

    @Override
    public ResponseResult getRecords(Integer type) {
        Integer realType = type + 4;
        LambdaQueryWrapper<Goal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Goal::getIsFinish, realType);
        return ResponseResult.okResult(list(queryWrapper));
    }

    @Override
    public ResponseResult scheduleAddMoney(CycleDto dto) {
        Goal goal = getById(dto.getGoalId());
        goal.setCycle(dto.getDays());
        goal.setCycleNum(dto.getNum());
        updateById(goal);
        return ResponseResult.okResult("设置成功");
    }

    @Override
    public ResponseResult normalAddMoney(CycleDto dto) {
        Goal goal = getById(dto.getGoalId());
        Double realNum = dto.getNum();
        if (goal.getNowNum() + dto.getNum() < goal.getGoalNum())
            goal.setNowNum(goal.getNowNum() + dto.getNum());
        else {
            realNum = goal.getGoalNum() - goal.getNowNum();
            goal.setNowNum(goal.getGoalNum());
            goal.setIsFinish(1);
        }
        updateById(goal);
        User user = userService.getById(dto.getUserId());
        Integer bookId = user.getDefaultBookId();
        Bill bill = new Bill();
        bill.setBookId(bookId);
        bill.setNum(realNum);
        bill.setTypeOne(1);
        bill.setTypeTwo("存入目标");
        bill.setText("于" + DateUtil.today() + "存入" + realNum + "元到" + goal.getText() + "中");
        bill.setPayWay(5);
        billService.add2(bill);
        return ResponseResult.okResult("存入成功");
    }
}




