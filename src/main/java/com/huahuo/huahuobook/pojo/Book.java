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
 * @TableName book
 */
@TableName(value ="book")
@Data
public class Book implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 1 正常 2家庭（共享） 3临时
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 
     */
    @TableField(value = "create_time")
    private String createTime;

    @TableField(value = "content")
    private String content;
    /**
     * 
     */
    @TableField(value = "name")
    private String name;
    @TableField(value = " total_income")
    private Double totalIncome;

    @TableField(value = "total_expense")
    private Double totalExpense;

    /**
     * 
     */
    @TableField(value = "img")
    private String img;

    /**
     * 
     */
    @TableField(value = "temp_code")
    private String tempCode;

    /**
     * 预算
     */
    @TableField(value = "budget")
    private Double budget;

    /**
     * 余额
     */
    @TableField(value = "balance")
    private Double balance;

    /**
     * 
     */
    @TableField(value = "food_budget")
    private Double foodBudget;

    /**
     * 
     */
    @TableField(value = "traffic_budget")
    private Double trafficBudget;

    /**
     * 
     */
    @TableField(value = "play_budget")
    private Double playBudget;

    /**
     * 
     */
    @TableField(value = "shopping_budget")
    private Double shoppingBudget;

    /**
     * 
     */
    @TableField(value = "culture_budget")
    private Double cultureBudget;

    /**
     * 
     */
    @TableField(value = "medical_budget")
    private Double medicalBudget;

    /**
     * 
     */
    @TableField(value = "house_budget")
    private Double houseBudget;

    /**
     * 
     */
    @TableField(value = "live_budget")
    private Double liveBudget;

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
        Book other = (Book) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getImg() == null ? other.getImg() == null : this.getImg().equals(other.getImg()))
            && (this.getTempCode() == null ? other.getTempCode() == null : this.getTempCode().equals(other.getTempCode()))
            && (this.getBudget() == null ? other.getBudget() == null : this.getBudget().equals(other.getBudget()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getFoodBudget() == null ? other.getFoodBudget() == null : this.getFoodBudget().equals(other.getFoodBudget()))
            && (this.getTrafficBudget() == null ? other.getTrafficBudget() == null : this.getTrafficBudget().equals(other.getTrafficBudget()))
            && (this.getPlayBudget() == null ? other.getPlayBudget() == null : this.getPlayBudget().equals(other.getPlayBudget()))
            && (this.getShoppingBudget() == null ? other.getShoppingBudget() == null : this.getShoppingBudget().equals(other.getShoppingBudget()))
            && (this.getCultureBudget() == null ? other.getCultureBudget() == null : this.getCultureBudget().equals(other.getCultureBudget()))
            && (this.getMedicalBudget() == null ? other.getMedicalBudget() == null : this.getMedicalBudget().equals(other.getMedicalBudget()))
            && (this.getHouseBudget() == null ? other.getHouseBudget() == null : this.getHouseBudget().equals(other.getHouseBudget()))
            && (this.getLiveBudget() == null ? other.getLiveBudget() == null : this.getLiveBudget().equals(other.getLiveBudget()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getImg() == null) ? 0 : getImg().hashCode());
        result = prime * result + ((getTempCode() == null) ? 0 : getTempCode().hashCode());
        result = prime * result + ((getBudget() == null) ? 0 : getBudget().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getFoodBudget() == null) ? 0 : getFoodBudget().hashCode());
        result = prime * result + ((getTrafficBudget() == null) ? 0 : getTrafficBudget().hashCode());
        result = prime * result + ((getPlayBudget() == null) ? 0 : getPlayBudget().hashCode());
        result = prime * result + ((getShoppingBudget() == null) ? 0 : getShoppingBudget().hashCode());
        result = prime * result + ((getCultureBudget() == null) ? 0 : getCultureBudget().hashCode());
        result = prime * result + ((getMedicalBudget() == null) ? 0 : getMedicalBudget().hashCode());
        result = prime * result + ((getHouseBudget() == null) ? 0 : getHouseBudget().hashCode());
        result = prime * result + ((getLiveBudget() == null) ? 0 : getLiveBudget().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", createTime=").append(createTime);
        sb.append(", name=").append(name);
        sb.append(", img=").append(img);
        sb.append(", tempCode=").append(tempCode);
        sb.append(", budget=").append(budget);
        sb.append(", balance=").append(balance);
        sb.append(", foodBudget=").append(foodBudget);
        sb.append(", trafficBudget=").append(trafficBudget);
        sb.append(", playBudget=").append(playBudget);
        sb.append(", shoppingBudget=").append(shoppingBudget);
        sb.append(", cultureBudget=").append(cultureBudget);
        sb.append(", medicalBudget=").append(medicalBudget);
        sb.append(", houseBudget=").append(houseBudget);
        sb.append(", liveBudget=").append(liveBudget);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}