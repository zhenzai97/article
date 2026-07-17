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
 * 应聘
 */
@TableName(value = "applicant")
@Data
public class Applicant implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 应聘人姓名 */
    private String name;

    /** 联系电话（旧 candidate_phone） */
    private String mobile;

    /** 期望薪资范围 */
    private String salaryRange;

    /** 公司 id */
    private Long companyId;

    /** 公司名称（冗余） */
    private String companyName;

    /** 招聘岗位 id（旧 jop_id） */
    private Long recruitmentId;

    /** 岗位名称（冗余） */
    private String recruitmentName;

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
