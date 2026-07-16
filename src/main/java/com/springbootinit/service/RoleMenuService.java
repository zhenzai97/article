package com.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springbootinit.model.entity.RoleMenu;
import java.util.List;

/**
 * 角色菜单关联服务
 */
public interface RoleMenuService extends IService<RoleMenu> {

    /**
     * 查询角色已分配的菜单 id
     */
    List<Long> listMenuIdsByRoleId(Long roleId);

    /**
     * 覆盖保存角色菜单（先删后插）
     */
    void assignMenus(Long roleId, List<Long> menuIds, Long createUserId);

    /**
     * 按角色删除关联
     */
    void removeByRoleId(Long roleId);
}
