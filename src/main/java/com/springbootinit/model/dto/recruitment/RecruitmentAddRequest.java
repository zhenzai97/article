package com.springbootinit.model.dto.recruitment;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class RecruitmentAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 招聘标题
     */
    private String name;

    /**
     * 薪资范围（前端约定字符串，如 5000-6000）
     */
    private String salaryRange;

    /**
     * 工作经验（前端枚举：1-3年/3-5年/5-10年/10-15年/15-20年/无经验）
     */
    private String workingHours;

    /**
     * 学历（前端枚举：小学/初中/高中/大专/大学本科/研究生/博士研究生）
     */
    private String qualification;

    /**
     * 公司id
     */
    private Long companyId;

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
     * 员工福利
     */
    private String employeeWelfare;

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

}
