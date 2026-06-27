package com.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springbootinit.model.dto.role.RoleQueryRequest;
import com.springbootinit.model.entity.Role;
import com.springbootinit.model.vo.RoleVO;

/**
 * 角色服务
 *
 */
public interface RoleService extends IService<Role> {

    /**
     * 校验数据
     *
     * @param role
     * @param add  对创建的数据进行校验
     */
    void validRole(Role role, boolean add);

    /**
     * 获取查询条件
     *
     * @param roleQueryRequest
     * @return
     */
    QueryWrapper<Role> getQueryWrapper(RoleQueryRequest roleQueryRequest);

    /**
     * 获取角色封装
     *
     * @param role
     * @return
     */
    RoleVO getRoleVO(Role role);

    /**
     * 分页获取角色封装
     *
     * @param rolePage
     * @return
     */
    Page<RoleVO> getRoleVOPage(Page<Role> rolePage);
}
