package com.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.constant.UserConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.mapper.MenuMapper;
import com.springbootinit.model.dto.menu.MenuQueryRequest;
import com.springbootinit.model.entity.Menu;
import com.springbootinit.model.entity.Role;
import com.springbootinit.model.entity.User;
import com.springbootinit.model.vo.MenuVO;
import com.springbootinit.service.MenuService;
import com.springbootinit.service.RoleMenuService;
import com.springbootinit.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;

@Service
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    private RoleService roleService;

    @Resource
    private RoleMenuService roleMenuService;

    @Override
    public void validMenu(Menu menu, boolean add){
        if (menu == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String menuName = menu.getMenuName();
        String path = menu.getPath();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(menuName, path), ErrorCode.PARAMS_ERROR);
        }
        Integer menuType  = menu.getMenuType();
        if (menuType == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }


    @Override
    public QueryWrapper<Menu> getQueryWrapper(MenuQueryRequest menuQueryRequest) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (menuQueryRequest == null) {
            return queryWrapper;
        }
        Long id = menuQueryRequest.getId();
        String menuName = menuQueryRequest.getMenuName();
        String permCode = menuQueryRequest.getPermCode();
        Integer menuType = menuQueryRequest.getMenuType();
        queryWrapper.eq(id != null,"id",id);
        queryWrapper.eq(StringUtils.isNotBlank(permCode),"permCode",menuName);
        queryWrapper.eq(menuType != null,"menuType",menuType);
        queryWrapper.like(StringUtils.isNotBlank(menuName),"menuName",menuName);
        return  queryWrapper;
    }


    @Override
    public MenuVO getMenuVO(Menu menu) {
        if (menu == null) {
            return null;
        }
        MenuVO menuVo = new MenuVO();
        BeanUtils.copyProperties(menu, menuVo);
        return menuVo;
    }

    @Override
    public  Page<MenuVO> getMenuVOPage(Page<Menu> menuPage){
        List<Menu> menuList = menuPage.getRecords();
        Page<MenuVO> menuVOPage = new Page<>(menuPage.getCurrent(), menuPage.getSize(), menuPage.getTotal());
        if (CollUtil.isEmpty(menuList)) {
            return menuVOPage;
        }
        List<MenuVO> menuVOList =menuList.stream().map(this::getMenuVO).collect(Collectors.toList());
        menuVOPage.setRecords(menuVOList);
        return menuVOPage;
    }

    @Override
    public List<MenuVO> buildMenuTree(List<Menu> menuList) {
        if (CollUtil.isEmpty(menuList)) {
            return new ArrayList<>();
        }
        List<MenuVO> voList = menuList.stream().map(this::getMenuVO).collect(Collectors.toList());
        Map<Long, List<MenuVO>> parentMap = voList.stream()
                .collect(Collectors.groupingBy(vo -> vo.getParentId() == null ? 0L : vo.getParentId()));
        voList.forEach(vo -> vo.setChildren(parentMap.get(vo.getId())));
        return parentMap.getOrDefault(0L, new ArrayList<>());
    }

    @Override
    public List<MenuVO> listMyMenuTree(User loginUser) {
        if (loginUser == null || StringUtils.isBlank(loginUser.getUserRole())) {
            return new ArrayList<>();
        }
        // 兼容旧逻辑：userRole=admin 视为超管，返回全部启用菜单
        if (UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole())) {
            return buildEnabledMenuTree();
        }
        Role role = roleService.getOne(new QueryWrapper<Role>()
                .eq("roleCode", loginUser.getUserRole())
                .eq("status", 1)
                .last("LIMIT 1"));
        if (role == null) {
            return new ArrayList<>();
        }
        if (role.getIsSuperAdmin() != null && role.getIsSuperAdmin() == 1) {
            return buildEnabledMenuTree();
        }
        List<Long> menuIds = roleMenuService.listMenuIdsByRoleId(role.getId());
        if (CollUtil.isEmpty(menuIds)) {
            return new ArrayList<>();
        }
        return buildMenuTree(listMenusWithAncestors(menuIds));
    }

    private List<MenuVO> buildEnabledMenuTree() {
        List<Menu> menuList = this.list(new QueryWrapper<Menu>()
                .eq("status", 1)
                .orderByAsc("sort"));
        return buildMenuTree(menuList);
    }

    /**
     * 按菜单 id 查询启用菜单，并补齐祖先节点以便建树
     */
    private List<Menu> listMenusWithAncestors(List<Long> menuIds) {
        List<Menu> allEnabled = this.list(new QueryWrapper<Menu>()
                .eq("status", 1)
                .orderByAsc("sort"));
        Map<Long, Menu> menuMap = allEnabled.stream()
                .collect(Collectors.toMap(Menu::getId, m -> m, (a, b) -> a));
        Set<Long> idSet = new HashSet<>();
        for (Long menuId : menuIds) {
            Long currentId = menuId;
            while (currentId != null && currentId > 0 && idSet.add(currentId)) {
                Menu menu = menuMap.get(currentId);
                if (menu == null) {
                    break;
                }
                currentId = menu.getParentId();
            }
        }
        return allEnabled.stream()
                .filter(m -> idSet.contains(m.getId()))
                .collect(Collectors.toList());
    }
}
