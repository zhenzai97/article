package com.springbootinit.model.dto.applicant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApplicantQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /** 应聘人姓名 */
    private String name;

    /** 联系电话 */
    private String mobile;

    /** 公司 id */
    private Long companyId;

    /** 招聘岗位 id */
    private Long recruitmentId;

    /** 期望薪资 */
    private String salaryRange;

    @JsonProperty("cStartTime")
    private String cStartTime;

    @JsonProperty("cEndTime")
    private String cEndTime;

    @JsonProperty("upStartTime")
    private String upStartTime;

    @JsonProperty("upEndTime")
    private String upEndTime;
}
