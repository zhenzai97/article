package com.springbootinit.controller;


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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/menu")
@Slf4j
public class MenuController {

    @Resource
    private UserService userService;

    @Resource
    private MenuService menuService;
    /**
     * 添加菜单
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addMenu(@RequestBody MenuAddRequest menuAddRequest, HttpServletRequest request) {
        if (menuAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuAddRequest, menu);
        menuService.validMenu(menu, true);
        User loginUser = userService.getLoginUser(request);
        menu.setCreateUserId(loginUser.getId());

        if (menu.getStatus() == null) {
            menu.setStatus(1);
        }
        if (menu.getVisible() == null) {
            menu.setVisible(1);
        }
        boolean result =  menuService.save(menu);
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
        Menu  oldMenu = menuService.getById(id);
        ThrowUtils.throwIf(oldMenu == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = menuService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }



    /**
     * 修改菜单
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
     *  分页获取菜单列表
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Menu>>  listMenuByPage(@RequestBody MenuQueryRequest menuQueryRequest) {
        long current = menuQueryRequest.getCurrent();
        long size = menuQueryRequest.getPageSize();
        Page<Menu> menuPage = menuService.page(new Page<>(current,size),menuService.getQueryWrapper(menuQueryRequest));
        return ResultUtils.success(menuPage);
    }


    /**
     * 分页获取菜单列表（封装类）
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<MenuVO>> listMenuVoByPage(@RequestBody MenuQueryRequest menuQueryRequest) {
        long current = menuQueryRequest.getCurrent();
        long size = menuQueryRequest.getPageSize();
        Page<Menu> menuPage = menuService.page(new Page<>(current, size),
                menuService.getQueryWrapper(menuQueryRequest));
        return ResultUtils.success(menuService.getMenuVOPage(menuPage));
    }

    /**
     * 获取菜单树（后台菜单管理、上级菜单下拉等）
     */
    @GetMapping("/list/tree/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<MenuVO>> listMenuTreeVO() {
        List<Menu> menuList = menuService.list(menuService.getQueryWrapper(new MenuQueryRequest()));
        return ResultUtils.success(menuService.buildMenuTree(menuList));
    }


}
