package com.springbootinit.model.dto.applicant;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApplicantAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 应聘人姓名 */
    private String name;

    /** 联系电话 */
    private String mobile;

    /** 期望薪资范围 */
    private String salaryRange;

    /** 公司 id */
    private Long companyId;

    /** 招聘岗位 id */
    private Long recruitmentId;
}
