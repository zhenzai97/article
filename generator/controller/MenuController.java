package com.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.annotation.AuthCheck;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.constant.UserConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.model.dto.menu.MenuAddRequest;
import com.springbootinit.model.dto.menu.MenuQueryRequest;
import com.springbootinit.model.dto.menu.MenuUpdateRequest;
import com.springbootinit.model.entity.Menu;
import com.springbootinit.model.entity.User;
import com.springbootinit.model.vo.MenuVO;
import com.springbootinit.service.MenuService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单接口
 *
 */
@RestController
@RequestMapping("/menu")
@Slf4j
public class MenuController {

    @Resource
    private MenuService menuService;

    @Resource
    private UserService userService;

    /**
     * 创建菜单
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addMenu(@RequestBody MenuAddRequest menuAddRequest,
            HttpServletRequest request) {
        ThrowUtils.throwIf(menuAddRequest == null, ErrorCode.PARAMS_ERROR);
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuAddRequest, menu);
        menuService.validMenu(menu, true);
        User loginUser = userService.getLoginUser(request);
        menu.setCreateUserId(loginUser.getId());
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        if (menu.getSort() == null) {
            menu.setSort(0);
        }
        if (menu.getVisible() == null) {
            menu.setVisible(1);
        }
        if (menu.getStatus() == null) {
            menu.setStatus(1);
        }
        if (menu.getIsCache() == null) {
            menu.setIsCache(0);
        }
        boolean result = menuService.save(menu);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(menu.getId());
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/delete/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteMenu(@PathVariable("id") long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Menu oldMenu = menuService.getById(id);
        ThrowUtils.throwIf(oldMenu == null, ErrorCode.NOT_FOUND_ERROR);
        long childCount = menuService.count(new QueryWrapper<Menu>().eq("parentId", id));
        ThrowUtils.throwIf(childCount > 0, ErrorCode.OPERATION_ERROR, "存在子菜单，无法删除");
        boolean result = menuService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新菜单
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateMenu(@RequestBody MenuUpdateRequest menuUpdateRequest) {
        if (menuUpdateRequest == null || menuUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Menu oldMenu = menuService.getById(menuUpdateRequest.getId());
        ThrowUtils.throwIf(oldMenu == null, ErrorCode.NOT_FOUND_ERROR);
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuUpdateRequest, menu);
        menuService.validMenu(menu, false);
        boolean result = menuService.updateById(menu);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取菜单
     */
    @GetMapping("/get/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<MenuVO> getMenuVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        Menu menu = menuService.getById(id);
        ThrowUtils.throwIf(menu == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(menuService.getMenuVO(menu));
    }

    /**
     * 获取菜单树
     */
    @GetMapping("/list/tree/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<MenuVO>> listMenuTreeVO() {
        List<Menu> menuList = menuService.list(menuService.getQueryWrapper(new MenuQueryRequest()));
        return ResultUtils.success(menuService.buildMenuTree(menuList));
    }

    /**
     * 分页获取菜单列表
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Menu>> listMenuByPage(@RequestBody MenuQueryRequest menuQueryRequest) {
        long current = menuQueryRequest.getCurrent();
        long size = menuQueryRequest.getPageSize();
        Page<Menu> menuPage = menuService.page(new Page<>(current, size),
                menuService.getQueryWrapper(menuQueryRequest));
        return ResultUtils.success(menuPage);
    }

    /**
     * 分页获取菜单列表（封装类）
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<MenuVO>> listMenuVOByPage(
            @RequestBody MenuQueryRequest menuQueryRequest) {
        long current = menuQueryRequest.getCurrent();
        long size = menuQueryRequest.getPageSize();
        Page<Menu> menuPage = menuService.page(new Page<>(current, size),
                menuService.getQueryWrapper(menuQueryRequest));
        return ResultUtils.success(menuService.getMenuVOPage(menuPage));
    }
}
