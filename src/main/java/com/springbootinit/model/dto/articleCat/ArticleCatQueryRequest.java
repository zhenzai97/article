package com.springbootinit.model.dto.articleCat;

import com.springbootinit.common.PageRequest;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询文章分类请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleCatQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 分类标识
     */
    private String sign;

    /**
     * 分类名称（模糊）
     */
    private String name;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
