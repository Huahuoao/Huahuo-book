package com.huahuo.huahuobook.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.models.auth.In;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "nick_name")
    private String nickName;
    @TableField(value = "age_type")
    private Integer ageType;
    @TableField(value = "default_book_id")
    private Integer defaultBookId;

    /**
     * 
     */
    @TableField(value = "password")
    private String password;

    /**
     * 
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 
     */
    @TableField(value = "create_time")
    private String createTime;

    /**
     * 
     */
    @TableField(value = "head_img")
    private String headImg;

    @TableField(value = "is_register")
    private Integer isRegister;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}