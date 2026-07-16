package com.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springbootinit.model.dto.menu.MenuQueryRequest;
import com.springbootinit.model.entity.Menu;
import com.springbootinit.model.entity.User;
import com.springbootinit.model.vo.MenuVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

public interface MenuService extends IService<Menu> {

    /**
     * 校验数据
     *
     * @param menu
     * @param add  对创建的数据进行校验
     */
    void validMenu(Menu menu, boolean add);

    /**
     * 获取查询条件
     * @param menuQueryRequest
     * @return
     */
    QueryWrapper<Menu> getQueryWrapper(MenuQueryRequest menuQueryRequest);

    /**
     * 获取菜单封装
     *
     * @param menu
     * @return
     */
    MenuVO getMenuVO(Menu menu);

    /**
     * 分页获取菜单封装（扁平列表，用于分页场景）
     *
     * @param menPage
     * @return
     */
    Page<MenuVO> getMenuVOPage(Page<Menu> menPage);

    /**
     * 构建菜单树（用于后台菜单管理、侧栏展示）
     *
     * @param menuList 菜单列表
     * @return 根节点列表，子节点在 children 中
     */
    List<MenuVO> buildMenuTree(List<Menu> menuList);

    /**
     * 当前登录用户可见菜单树（含目录/菜单/按钮）
     * <p>桥接：user.userRole 对应 role.roleCode；超管返回全部启用菜单
     */
    List<MenuVO> listMyMenuTree(User loginUser);
}
