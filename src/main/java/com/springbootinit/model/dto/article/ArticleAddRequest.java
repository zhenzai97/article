package com.springbootinit.model.dto.article;

import java.io.Serializable;
import lombok.Data;

/**
 * 创建文章请求
 *
 */
@Data
public class ArticleAddRequest implements Serializable {

    /**
     * 分类 id
     */
    private Long categoryId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 封面图
     */
    private String cover;

    /**
     * 视频地址
     */
    private String video;

    /**
     * 排序
     */
    private Integer sort;

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

    private static final long serialVersionUID = 1L;
}
