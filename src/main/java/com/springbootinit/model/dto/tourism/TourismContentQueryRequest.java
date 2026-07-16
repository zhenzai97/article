package com.springbootinit.model.dto.tourism;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springbootinit.common.PageRequest;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文旅内容查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TourismContentQueryRequest extends PageRequest implements Serializable {

    private Long id;

    /** 类型 */
    private String type;

    /** 名称（模糊） */
    private String name;

    /** 状态 */
    private Integer status;

    /** 是否推荐 */
    private Integer isRecommend;

    @JsonProperty("cStartTime")
    private String cStartTime;

    @JsonProperty("cEndTime")
    private String cEndTime;

    @JsonProperty("upStartTime")
    private String upStartTime;

    @JsonProperty("upEndTime")
    private String upEndTime;

    private static final long serialVersionUID = 1L;
}
