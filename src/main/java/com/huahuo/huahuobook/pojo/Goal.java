package com.huahuo.huahuobook.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

/**
 * 
 * @TableName goal
 */
@TableName(value ="goal")
@Data
public class Goal implements Serializable {
    /**
     * 
     */
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
    @TableField(value = "goal_num")
    private Double goalNum;

    /**
     * 
     */
    @TableField(value = "now_num")
    private Double nowNum;

    /**
     * 
     */
    @TableField(value = "text")
    private String text;

    /**
     * 
     */
    @TableField(value = "img")
    private String img;

    /**
     * 
     */
    @TableField(value = "create_time")
    private String createTime;

    /**
     * 
     */
    @TableField(value = "end_time")
    private String endTime;
    @TableField(value = "cycle_num")
    private Double cycleNum;
    @TableField(value = "is_finish")
    private Integer isFinish;

    /**
     * 
     */
    @TableField(value = "update_time")
    private String updateTime;

    /**
     * 
     */
    @TableField(value = "cycle")
    private Integer cycle;

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
        Goal other = (Goal) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getGoalNum() == null ? other.getGoalNum() == null : this.getGoalNum().equals(other.getGoalNum()))
            && (this.getNowNum() == null ? other.getNowNum() == null : this.getNowNum().equals(other.getNowNum()))
            && (this.getText() == null ? other.getText() == null : this.getText().equals(other.getText()))
            && (this.getImg() == null ? other.getImg() == null : this.getImg().equals(other.getImg()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getCycle() == null ? other.getCycle() == null : this.getCycle().equals(other.getCycle()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getGoalNum() == null) ? 0 : getGoalNum().hashCode());
        result = prime * result + ((getNowNum() == null) ? 0 : getNowNum().hashCode());
        result = prime * result + ((getText() == null) ? 0 : getText().hashCode());
        result = prime * result + ((getImg() == null) ? 0 : getImg().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getCycle() == null) ? 0 : getCycle().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", goalNum=").append(goalNum);
        sb.append(", nowNum=").append(nowNum);
        sb.append(", text=").append(text);
        sb.append(", img=").append(img);
        sb.append(", createTime=").append(createTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", cycle=").append(cycle);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}