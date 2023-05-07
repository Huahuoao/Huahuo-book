package com.huahuo.huahuobook.dto;

import lombok.Data;

@Data
public class UserBaseInfo {
    Integer userId;
    String headImg;
    String nickName;
    Integer ageType;
    Integer defaultBookId;
}