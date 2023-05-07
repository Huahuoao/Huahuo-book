package com.huahuo.huahuobook.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @作者 花火
 * @创建日期 2023/1/28 16:23
 */
@Data
public class ShareBookDto implements Serializable {
    Integer friendId;
    Integer bookId;
}
