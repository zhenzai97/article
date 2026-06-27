package com.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.constant.CommonConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.mapper.RoleMapper;
import com.springbootinit.model.dto.role.RoleQueryRequest;
import com.springbootinit.model.entity.Role;
import com.springbootinit.model.vo.RoleVO;
import com.springbootinit.service.RoleService;
import com.springbootinit.utils.SqlUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 角色服务实现
 *
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public void validRole(Role role, boolean add) {
        ThrowUtils.throwIf(role == null, ErrorCode.PARAMS_ERROR);
        String roleCode = role.getRoleCode();
        String roleName = role.getRoleName();
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(roleCode, roleName), ErrorCode.PARAMS_ERROR, "角色编码和名称不能为空");
        }
        if (StringUtils.isNotBlank(roleCode) && roleCode.length() > 64) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "角色编码过长");
        }
        if (StringUtils.isNotBlank(roleName) && roleName.length() > 128) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "角色名称过长");
        }
    }

    @Override
    public QueryWrapper<Role> getQueryWrapper(RoleQueryRequest roleQueryRequest) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (roleQueryRequest == null) {
            return queryWrapper;
        }
        Long id = roleQueryRequest.getId();
        String roleCode = roleQueryRequest.getRoleCode();
        String roleName = roleQueryRequest.getRoleName();
        Integer status = roleQueryRequest.getStatus();
        String sortField = roleQueryRequest.getSortField();
        String sortOrder = roleQueryRequest.getSortOrder();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(roleCode), "roleCode", roleCode);
        queryWrapper.like(StringUtils.isNotBlank(roleName), "roleName", roleName);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder), sortField);
        return queryWrapper;
    }

    @Override
    public RoleVO getRoleVO(Role role) {
        if (role == null) {
            return null;
        }
        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(role, roleVO);
        return roleVO;
    }

    @Override
    public Page<RoleVO> getRoleVOPage(Page<Role> rolePage) {
        List<Role> roleList = rolePage.getRecords();
        Page<RoleVO> roleVOPage = new Page<>(rolePage.getCurrent(), rolePage.getSize(),
                rolePage.getTotal());
        if (CollUtil.isEmpty(roleList)) {
            return roleVOPage;
        }
        List<RoleVO> roleVOList = roleList.stream().map(this::getRoleVO)
                .collect(Collectors.toList());
        roleVOPage.setRecords(roleVOList);
        return roleVOPage;
    }
}
