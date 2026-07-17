package com.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 招聘
 */
@TableName(value = "recruitment")
@Data
public class Recruitment implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 招聘标题 */
    private String name;

    /**
     * 薪资范围（前端约定字符串，直接入库）
     * 示例：5000-6000
     */
    private String salaryRange;

    /**
     * 工作经验/时长（前端枚举字符串，直接入库）
     * 枚举：1-3年、3-5年、5-10年、10-15年、15-20年、无经验
     */
    private String workingHours;

    /**
     * 学历（前端枚举字符串，直接入库）
     * 枚举：小学、初中、高中、大专、大学本科、研究生、博士研究生
     */
    private String qualification;

    /** 公司 id */
    private Long companyId;

    /** 公司名称（冗余） */
    private String companyName;

    /** 排序 */
    private Integer sort;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    /** 薪资详情 */
    private String salaryContent;

    /** 岗位详情 */
    private String positionContent;

    /** 员工福利 */
    private String employeeWelfare;

    /** 旧 CMS _id */
    private String sourceId;

    private Long createUserId;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
