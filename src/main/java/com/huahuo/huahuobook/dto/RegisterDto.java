package com.huahuo.huahuobook.dto;

import com.huahuo.huahuobook.pojo.Goal;
import lombok.Data;

/**
 * @作者 花火
 * @创建日期 2023/3/9 23:32
 */
@Data
public class RegisterDto {
    String nickName;
    Integer ageType;
    Integer userId;
    String headImg;

}
