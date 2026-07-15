package com.springbootinit.model.dto.articleCat;

import java.io.Serializable;
import lombok.Data;

/**
 * 编辑文章分类请求
 *
 */
@Data
public class ArticleCatEditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;
}
