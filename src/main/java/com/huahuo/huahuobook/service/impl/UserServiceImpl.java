package com.huahuo.huahuobook.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.dto.BookDto;
import com.huahuo.huahuobook.dto.LoginDto;
import com.huahuo.huahuobook.dto.RegisterDto;
import com.huahuo.huahuobook.pojo.Goal;
import com.huahuo.huahuobook.pojo.User;
import com.huahuo.huahuobook.service.BookService;
import com.huahuo.huahuobook.service.UserService;
import com.huahuo.huahuobook.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2023-01-23 23:54:55
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    BookService bookService;

    @Override
    public ResponseResult<String> login(LoginDto dto) {
        User realUser = userMapper.selectByPhoneNumber(dto.getPhone());
        Integer id = null;
        if (realUser == null) {
            realUser = new User();
            realUser.setPhone(dto.getPhone());
            save(realUser);
            id = realUser.getId();
            return ResponseResult.okResult(201, id);
        }
        if (realUser.getIsRegister() == 0){
            id = realUser.getId();
            return ResponseResult.okResult(201, id);
        }
            id = realUser.getId();
        if (dto.getCode().equals("root")) //密码正确。生成token 返回
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", Integer.parseInt(realUser.getId().toString()));
            String token = JWTUtil.createToken(map, "huahuo".getBytes());
            if (JWTUtil.verify(token, "huahuo".getBytes())) {
                Map result = new HashMap();
                result.put("token", token);
                result.put("id", id);
                result.put("defaultBookId",realUser.getDefaultBookId());
                return ResponseResult.okResult(result);

            }
            return ResponseResult.errorResult(401, "token不存在");
        }
        return ResponseResult.errorResult(302, "验证码错误");


    }

    @Override
    public ResponseResult<String> register(RegisterDto registerDto) {
        User user = getById(registerDto.getUserId());
        user.setNickName(registerDto.getNickName());
        user.setAgeType(registerDto.getAgeType());
        user.setHeadImg(registerDto.getHeadImg());
        user.setCreateTime(DateUtil.now());
        //目标暂时不接；//俩种一种直接传，一种选择的

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", Integer.parseInt(registerDto.getUserId().toString()));
        String token = JWTUtil.createToken(map, "huahuo".getBytes());
        BookDto dto = new BookDto();
        dto.setUserId(registerDto.getUserId());
        dto.setType(1);
        dto.setName(registerDto.getNickName() + "的账本");

        Integer newBook1 = bookService.createNewBook1(dto);
        user.setDefaultBookId(newBook1);
        user.setIsRegister(1);
        updateById(user);
        return ResponseResult.okResult(token);
    }
}




