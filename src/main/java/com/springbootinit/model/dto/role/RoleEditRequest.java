package com.springbootinit.model.dto.role;

import java.io.Serializable;
import lombok.Data;

/**
 * 编辑角色请求
 *
 */
@Data
public class RoleEditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 数据范围：1-全部 2-本部门 3-仅本人
     */
    private Integer dataScope;

    private static final long serialVersionUID = 1L;
}
