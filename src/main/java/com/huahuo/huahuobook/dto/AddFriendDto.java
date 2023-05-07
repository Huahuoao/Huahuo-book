package com.huahuo.huahuobook.dto;

import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

/**
 * @作者 花火
 * @创建日期 2023/1/28 16:04
 */
@Data
public class AddFriendDto implements Serializable {
    String username;
    Integer userId;
    Integer type;
}
