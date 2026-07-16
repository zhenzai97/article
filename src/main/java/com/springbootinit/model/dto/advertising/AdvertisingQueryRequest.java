package com.springbootinit.model.dto.advertising;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springbootinit.common.PageRequest;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 运营广告查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdvertisingQueryRequest extends PageRequest implements Serializable {

    private Long id;

    /** 运营位 id */
    private Long spaceId;

    /** 标题（模糊） */
    private String name;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    /** 创建时间范围-开始 */
    @JsonProperty("cStartTime")
    private String cStartTime;

    /** 创建时间范围-结束 */
    @JsonProperty("cEndTime")
    private String cEndTime;

    /** 更新时间范围-开始 */
    @JsonProperty("upStartTime")
    private String upStartTime;

    /** 更新时间范围-结束 */
    @JsonProperty("upEndTime")
    private String upEndTime;

    /** 展示开始日期范围-起 */
    @JsonProperty("sStartTime")
    private String sStartTime;

    /** 展示开始日期范围-止 */
    @JsonProperty("sEndTime")
    private String sEndTime;

    /** 展示结束日期范围-起 */
    @JsonProperty("eStartTime")
    private String eStartTime;

    /** 展示结束日期范围-止 */
    @JsonProperty("eEndTime")
    private String eEndTime;

    private static final long serialVersionUID = 1L;
}
