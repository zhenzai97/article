package com.springbootinit.model.dto.article;

import java.io.Serializable;
import lombok.Data;

/**
 * 编辑文章请求
 *
 */
@Data
public class ArticleEditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

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
     * 营业执照
     */
    private String license;

    /**
     * 视频地址
     */
    private String video;

    /**
     * 排序
     */
    private Integer sort;

    private static final long serialVersionUID = 1L;
}
