package com.springbootinit.model.dto.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springbootinit.common.PageRequest;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询文章请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 分类 id
     */
    private Long categoryId;

    /**
     * 标题（模糊）
     */
    private String title;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;

    /**
     * 是否首页展示：0-否 1-是
     */
    private Integer isHome;

    /**
     * 是否置顶：0-否 1-是
     */
    private Integer isTop;

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

    private static final long serialVersionUID = 1L;
}
