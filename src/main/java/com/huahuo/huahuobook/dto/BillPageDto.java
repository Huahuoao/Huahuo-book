package com.huahuo.huahuobook.dto;


import lombok.Data;

/**
 * @作者 花火
 * @创建日期 2023/1/27 18:19
 */
@Data
public class BillPageDto extends PageRequestDto {
    private Integer bookId;
    private String keyword; // 搜索关键字
    private Integer typeOne;
    private String typeTwo;
    private String beginTime;
    private String endTime;
    private Integer isCollect;
    private Integer payWay;
}
