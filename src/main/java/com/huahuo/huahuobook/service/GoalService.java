package com.huahuo.huahuobook.service;

import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.dto.CycleDto;
import com.huahuo.huahuobook.pojo.Goal;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
* @author Administrator
* @description 针对表【goal】的数据库操作Service
* @createDate 2023-03-09 23:41:13
*/
public interface GoalService extends IService<Goal> {
    public ResponseResult getRecords(@PathVariable Integer type);
    public ResponseResult scheduleAddMoney(@RequestBody CycleDto dto);
    public ResponseResult normalAddMoney(@RequestBody CycleDto dto);
}
