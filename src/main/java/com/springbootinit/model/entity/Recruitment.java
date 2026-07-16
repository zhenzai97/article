package com.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 招聘
 */

@TableName(value = "recruitment")
@Data
public class Recruitment  implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 招聘标题
     */
    private String name;

    /**
     * 薪资范围
     */
    private String salaryRange;

    /**
     * 工作时长
     */
    private String workingHours;

    /**
     * 学历
     */
    private String qualification;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;


    /**
     * 薪资详情
     */
    private String salaryContent;


    /**
     * 岗位详情
     */
    private String positionContent;

    /**
     * 创建人 id
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
