package com.huahuo.huahuobook.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huahuo.huahuobook.pojo.LogInfo;
import com.huahuo.huahuobook.service.LogInfoService;
import com.huahuo.huahuobook.mapper.LogInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【log_info】的数据库操作Service实现
* @createDate 2023-03-09 17:25:03
*/
@Service
public class LogInfoServiceImpl extends ServiceImpl<LogInfoMapper, LogInfo>
    implements LogInfoService{

}




