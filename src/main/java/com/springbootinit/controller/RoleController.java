package com.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.annotation.AuthCheck;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.constant.UserConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.model.dto.role.RoleAddRequest;
import com.springbootinit.model.dto.role.RoleAssignMenuRequest;
import com.springbootinit.model.dto.role.RoleQueryRequest;
import com.springbootinit.model.dto.role.RoleUpdateRequest;
import com.springbootinit.model.entity.Role;
import com.springbootinit.model.entity.User;
import com.springbootinit.model.vo.RoleVO;
import com.springbootinit.service.RoleMenuService;
import com.springbootinit.service.RoleService;
import com.springbootinit.service.UserService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色接口
 *
 */
@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {

    @Resource
    private RoleService roleService;

    @Resource
    private RoleMenuService roleMenuService;

    @Resource
    private UserService userService;

    /**
     * 创建角色
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addRole(@RequestBody RoleAddRequest roleAddRequest,
            HttpServletRequest request) {
        ThrowUtils.throwIf(roleAddRequest == null, ErrorCode.PARAMS_ERROR);
        Role role = new Role();
        BeanUtils.copyProperties(roleAddRequest, role);
        roleService.validRole(role, true);
        User loginUser = userService.getLoginUser(request);
        role.setCreateUserId(loginUser.getId());
        if (role.getStatus() == null) {
            role.setStatus(1);
        }
        if (role.getSort() == null) {
            role.setSort(0);
        }
        if (role.getIsBuiltin() == null) {
            role.setIsBuiltin(0);
        }
        if (role.getIsSuperAdmin() == null) {
            role.setIsSuperAdmin(0);
        }
        if (role.getDataScope() == null) {
            role.setDataScope(1);
        }
        boolean result = roleService.save(role);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(role.getId());
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/delete/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteRole(@PathVariable("id") long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Role oldRole = roleService.getById(id);
        ThrowUtils.throwIf(oldRole == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(oldRole.getIsBuiltin() != null && oldRole.getIsBuiltin() == 1,
                ErrorCode.OPERATION_ERROR, "内置角色不可删除");
        boolean result = roleService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        roleMenuService.removeByRoleId(id);
        return ResultUtils.success(true);
    }

    /**
     * 更新角色
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateRole(@RequestBody RoleUpdateRequest roleUpdateRequest) {
        if (roleUpdateRequest == null || roleUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Role oldRole = roleService.getById(roleUpdateRequest.getId());
        ThrowUtils.throwIf(oldRole == null, ErrorCode.NOT_FOUND_ERROR);
        Role role = new Role();
        BeanUtils.copyProperties(roleUpdateRequest, role);
        roleService.validRole(role, false);
        boolean result = roleService.updateById(role);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取角色
     */
    @GetMapping("/get/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<RoleVO> getRoleVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        Role role = roleService.getById(id);
        ThrowUtils.throwIf(role == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(roleService.getRoleVO(role));
    }

    /**
     * 分页获取角色列表
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Role>> listRoleByPage(@RequestBody RoleQueryRequest roleQueryRequest) {
        long current = roleQueryRequest.getCurrent();
        long size = roleQueryRequest.getPageSize();
        Page<Role> rolePage = roleService.page(new Page<>(current, size),
                roleService.getQueryWrapper(roleQueryRequest));
        return ResultUtils.success(rolePage);
    }

    /**
     * 分页获取角色列表（封装类）
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<RoleVO>> listRoleVOByPage(
            @RequestBody RoleQueryRequest roleQueryRequest) {
        long current = roleQueryRequest.getCurrent();
        long size = roleQueryRequest.getPageSize();
        Page<Role> rolePage = roleService.page(new Page<>(current, size),
                roleService.getQueryWrapper(roleQueryRequest));
        return ResultUtils.success(roleService.getRoleVOPage(rolePage));
    }

    /**
     * 查询角色已分配的菜单 id 列表
     */
    @GetMapping("/menuIds")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<Long>> listRoleMenuIds(@RequestParam("roleId") long roleId) {
        ThrowUtils.throwIf(roleId <= 0, ErrorCode.PARAMS_ERROR);
        Role role = roleService.getById(roleId);
        ThrowUtils.throwIf(role == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(roleMenuService.listMenuIdsByRoleId(roleId));
    }

    /**
     * 为角色分配菜单（覆盖保存，含目录/菜单/按钮）
     */
    @PostMapping("/assignMenu")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> assignMenu(@RequestBody RoleAssignMenuRequest request,
            HttpServletRequest httpServletRequest) {
        if (request == null || request.getRoleId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Role role = roleService.getById(request.getRoleId());
        ThrowUtils.throwIf(role == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(role.getIsSuperAdmin() != null && role.getIsSuperAdmin() == 1,
                ErrorCode.OPERATION_ERROR, "超级管理员拥有全部菜单，无需分配");
        User loginUser = userService.getLoginUser(httpServletRequest);
        roleMenuService.assignMenus(request.getRoleId(), request.getMenuIds(), loginUser.getId());
        return ResultUtils.success(true);
    }
}
