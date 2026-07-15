package com.springbootinit.model.dto.articleCat;

import java.io.Serializable;
import lombok.Data;

/**
 * 创建文章分类请求
 *
 */
@Data
public class ArticleCatAddRequest implements Serializable {

    /**
     * 分类标识
     */
    private String sign;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;
}
