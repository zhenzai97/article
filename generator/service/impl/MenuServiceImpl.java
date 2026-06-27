package com.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.constant.CommonConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.mapper.MenuMapper;
import com.springbootinit.model.dto.menu.MenuQueryRequest;
import com.springbootinit.model.entity.Menu;
import com.springbootinit.model.vo.MenuVO;
import com.springbootinit.service.MenuService;
import com.springbootinit.utils.SqlUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 菜单服务实现
 *
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public void validMenu(Menu menu, boolean add) {
        ThrowUtils.throwIf(menu == null, ErrorCode.PARAMS_ERROR);
        String menuName = menu.getMenuName();
        Integer menuType = menu.getMenuType();
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(menuName), ErrorCode.PARAMS_ERROR, "菜单名称不能为空");
            ThrowUtils.throwIf(menuType == null, ErrorCode.PARAMS_ERROR, "菜单类型不能为空");
        }
        if (StringUtils.isNotBlank(menuName) && menuName.length() > 128) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "菜单名称过长");
        }
        if (menuType != null && (menuType < 1 || menuType > 3)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "菜单类型无效");
        }
    }

    @Override
    public QueryWrapper<Menu> getQueryWrapper(MenuQueryRequest menuQueryRequest) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (menuQueryRequest == null) {
            return queryWrapper;
        }
        Long id = menuQueryRequest.getId();
        Long parentId = menuQueryRequest.getParentId();
        String menuName = menuQueryRequest.getMenuName();
        Integer menuType = menuQueryRequest.getMenuType();
        Integer visible = menuQueryRequest.getVisible();
        Integer status = menuQueryRequest.getStatus();
        String sortField = menuQueryRequest.getSortField();
        String sortOrder = menuQueryRequest.getSortOrder();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(parentId != null, "parentId", parentId);
        queryWrapper.like(StringUtils.isNotBlank(menuName), "menuName", menuName);
        queryWrapper.eq(menuType != null, "menuType", menuType);
        queryWrapper.eq(visible != null, "visible", visible);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder), sortField);
        if (!SqlUtils.validSortField(sortField)) {
            queryWrapper.orderByAsc("sort");
        }
        return queryWrapper;
    }

    @Override
    public MenuVO getMenuVO(Menu menu) {
        if (menu == null) {
            return null;
        }
        MenuVO menuVO = new MenuVO();
        BeanUtils.copyProperties(menu, menuVO);
        return menuVO;
    }

    @Override
    public Page<MenuVO> getMenuVOPage(Page<Menu> menuPage) {
        List<Menu> menuList = menuPage.getRecords();
        Page<MenuVO> menuVOPage = new Page<>(menuPage.getCurrent(), menuPage.getSize(),
                menuPage.getTotal());
        if (CollUtil.isEmpty(menuList)) {
            return menuVOPage;
        }
        List<MenuVO> menuVOList = menuList.stream().map(this::getMenuVO)
                .collect(Collectors.toList());
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
}
