package com.springbootinit.model.dto.recruitment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecruitmentQueryRequest  extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
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
     * 排序
     */
    private Integer sort;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;



    /**
     * 创建时间范围-开始
     */
    @JsonProperty("cStartTime")
    private String cStartTime;

    /**
     * 创建时间范围-结束
     */
    @JsonProperty("cEndTime")
    private String cEndTime;

    /**
     * 更新时间范围-开始
     */
    @JsonProperty("upStartTime")
    private String upStartTime;

    /**
     * 更新时间范围-结束
     */
    @JsonProperty("upEndTime")
    private String upEndTime;
}
