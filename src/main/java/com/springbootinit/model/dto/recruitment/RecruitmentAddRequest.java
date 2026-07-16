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

}
