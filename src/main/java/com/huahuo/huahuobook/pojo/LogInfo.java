package com.huahuo.huahuobook.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName log_info
 */
@TableName(value ="log_info")
@Data
public class LogInfo implements Serializable {
    /**
     * 
     */
    @TableField(value = "call_time")
    private String callTime;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 
     */
    @TableField(value = "module")
    private String module;

    /**
     * 
     */
    @TableField(value = "function_name")
    private String functionName;

    /**
     * 
     */
    @TableField(value = "func")
    private String func;

    /**
     * 
     */
    @TableField(value = "params")
    private String params;

    /**
     * 
     */
    @TableField(value = "waste_time")
    private Long wasteTime;
    @TableField(value = "user_name")
    private String username;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        LogInfo other = (LogInfo) that;
        return (this.getCallTime() == null ? other.getCallTime() == null : this.getCallTime().equals(other.getCallTime()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getModule() == null ? other.getModule() == null : this.getModule().equals(other.getModule()))
            && (this.getFunctionName() == null ? other.getFunctionName() == null : this.getFunctionName().equals(other.getFunctionName()))
            && (this.getFunc() == null ? other.getFunc() == null : this.getFunc().equals(other.getFunc()))
            && (this.getParams() == null ? other.getParams() == null : this.getParams().equals(other.getParams()))
            && (this.getWasteTime() == null ? other.getWasteTime() == null : this.getWasteTime().equals(other.getWasteTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCallTime() == null) ? 0 : getCallTime().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getModule() == null) ? 0 : getModule().hashCode());
        result = prime * result + ((getFunctionName() == null) ? 0 : getFunctionName().hashCode());
        result = prime * result + ((getFunc() == null) ? 0 : getFunc().hashCode());
        result = prime * result + ((getParams() == null) ? 0 : getParams().hashCode());
        result = prime * result + ((getWasteTime() == null) ? 0 : getWasteTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", callTime=").append(callTime);
        sb.append(", userId=").append(userId);
        sb.append(", module=").append(module);
        sb.append(", functionName=").append(functionName);
        sb.append(", func=").append(func);
        sb.append(", params=").append(params);
        sb.append(", wasteTime=").append(wasteTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}