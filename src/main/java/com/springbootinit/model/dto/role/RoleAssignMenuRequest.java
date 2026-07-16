package com.springbootinit.model.dto.role;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 角色分配菜单请求
 */
@Data
public class RoleAssignMenuRequest implements Serializable {

    /**
     * 角色 id
     */
    private Long roleId;

    /**
     * 菜单 id 列表（覆盖保存，含目录/菜单/按钮）
     */
    private List<Long> menuIds;

    private static final long serialVersionUID = 1L;
}
