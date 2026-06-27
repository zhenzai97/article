package com.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springbootinit.model.dto.menu.MenuQueryRequest;
import com.springbootinit.model.entity.Menu;
import com.springbootinit.model.vo.MenuVO;
import java.util.List;

/**
 * 菜单服务
 *
 */
public interface MenuService extends IService<Menu> {

    /**
     * 校验数据
     */
    void validMenu(Menu menu, boolean add);

    /**
     * 获取查询条件
     */
    QueryWrapper<Menu> getQueryWrapper(MenuQueryRequest menuQueryRequest);

    /**
     * 获取菜单封装
     */
    MenuVO getMenuVO(Menu menu);

    /**
     * 分页获取菜单封装
     */
    Page<MenuVO> getMenuVOPage(Page<Menu> menuPage);

    /**
     * 构建菜单树
     */
    List<MenuVO> buildMenuTree(List<Menu> menuList);
}
