package com.springbootinit.model.dto.role;

import java.io.Serializable;
import lombok.Data;

/**
 * 更新角色请求
 *
 */
@Data
public class RoleUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;

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
