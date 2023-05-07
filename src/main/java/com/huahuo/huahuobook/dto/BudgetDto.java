package com.huahuo.huahuobook.dto;

import lombok.Data;

/**
 * @作者 花火
 * @创建日期 2023/3/15 20:07
 */
@Data
public class BudgetDto {
    Integer bookId;
    Double budget;
    Double foodBudget;
    Double trafficBudget;
    Double playBudget;
    Double shoppingBudget;
    Double cultureBudget;
    Double medicalBudget;
    Double houseBudget;
    Double liveBudget;

}
