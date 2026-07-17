package com.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ApplicantVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /** 应聘人姓名 */
    private String name;

    /** 联系电话 */
    private String mobile;

    /** 期望薪资范围 */
    private String salaryRange;

    /** 公司 id */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 招聘岗位 id */
    private Long recruitmentId;

    /** 岗位名称 */
    private String recruitmentName;

    private Long createUserId;

    private Date createTime;

    private Date updateTime;
}
