package com.huahuo.huahuobook.dto;

import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

/**
 * @作者 花火
 * @创建日期 2023/1/29 13:04
 */
@Data
public class ListBookDto implements Serializable {
    Integer userId;
    Integer type;
}
