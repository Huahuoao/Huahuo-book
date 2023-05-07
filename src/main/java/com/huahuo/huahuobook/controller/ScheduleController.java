package com.huahuo.huahuobook.controller;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huahuo.huahuobook.pojo.Bill;
import com.huahuo.huahuobook.pojo.Goal;
import com.huahuo.huahuobook.pojo.User;
import com.huahuo.huahuobook.service.BillService;
import com.huahuo.huahuobook.service.BookService;
import com.huahuo.huahuobook.service.GoalService;
import com.huahuo.huahuobook.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @作者 花火
 * @创建日期 2023/3/13 8:27
 */
@Slf4j
@EnableScheduling
@Configuration
public class ScheduleController {
    @Autowired
    GoalService goalService;
    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;
    @Autowired
    BillService billService;
 //   @Scheduled(cron = "0 0 8 1 * ?")
@Scheduled(cron = "0 0/10 * * * ?")
void deleteUselessUser()
{
    List<User> list = userService.list();
    ArrayList<Integer> ids = new ArrayList<>();
    for (User user : list) {
        if(user.getNickName()==null)
        {
            ids.add(user.getId());
        }
    }
    userService.removeByIds(ids);
}
    @Scheduled(cron = "0 0 12 * * ?")
    void addGoal() {
        LambdaQueryWrapper<Goal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Goal::getCycleNum,0);
        List<Goal> list = goalService.list(queryWrapper);
        for (Goal goal : list) {
            //比较时间，超过计划时间就改。
            Date planTime = DateUtil.parse(goal.getEndTime());
            Date now = DateUtil.date();
            if (planTime.compareTo(now) < 0) {
                goal.setIsFinish(3);
            }
            if (goal.getIsFinish() != 0) continue;
            String lastTime = goal.getUpdateTime();
            String nowTime = DateUtil.today();
            Date date1 = DateUtil.parse(lastTime);
            Date date2 = DateUtil.parse(nowTime);
            Double realNum = goal.getCycleNum();
            Long betweenDay = DateUtil.between(date1, date2, DateUnit.DAY);
            if (betweenDay >= goal.getCycle()) ;
            {
                Double gap = goal.getGoalNum() - goal.getNowNum();
                if (gap <= goal.getCycleNum()) {
                    goal.setIsFinish(1);
                    goal.setNowNum(goal.getGoalNum());
                    realNum = gap;
                } else {
                    goal.setNowNum(goal.getNowNum() + goal.getCycleNum());
                }
                Bill bill = new Bill();
                User user = userService.getById(goal.getUserId());
                bill.setBookId(user.getDefaultBookId());
                bill.setNum(realNum);
                bill.setTypeOne(1);
                bill.setTypeTwo("存入目标");
                bill.setText("于" + DateUtil.today() + "存入" + realNum + "元到" + goal.getText() + "中");
                bill.setPayWay(5);
                billService.add2(bill);
                goal.setUpdateTime(DateUtil.today());
                goalService.updateById(goal);
            }

        }

    }
}
