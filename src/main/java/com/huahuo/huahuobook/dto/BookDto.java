package com.huahuo.huahuobook.dto;

import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

/**
 * @作者 花火
 * @创建日期 2023/1/27 17:31
 */
@Data
public class BookDto implements Serializable {
    Integer type;
    Integer userId;
    String name;
    String img;
    String content;
    Integer id;
}
