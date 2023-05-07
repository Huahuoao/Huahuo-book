package com.huahuo.huahuobook.service;

import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.dto.LoginDto;
import com.huahuo.huahuobook.dto.RegisterDto;
import com.huahuo.huahuobook.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service
* @createDate 2023-01-23 23:54:55
*/
public interface UserService extends IService<User> {
    ResponseResult<String> login(@RequestBody LoginDto dto);
    public ResponseResult<String> register(@RequestBody RegisterDto registerDto);
}
