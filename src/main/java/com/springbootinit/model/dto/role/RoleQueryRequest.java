package com.springbootinit.model.dto.role;

import com.springbootinit.common.PageRequest;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询角色请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称（模糊）
     */
    private String roleName;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
