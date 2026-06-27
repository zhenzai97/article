package com.springbootinit.model.dto.menu;

import lombok.Data;

import java.io.Serializable;

@Data
public class MenuAddRequest  implements Serializable {

    /**
     * 父菜单 id，0 表示根节点
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单类型：1-目录 2-菜单 3-按钮
     */
    private Integer menuType;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 显示排序
     */
    private Integer sort;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 权限字符
     */
    private String permCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 显示状态：0-隐藏 1-显示
     */
    private Integer visible;

    /**
     * 菜单状态：0-停用 1-正常
     */
    private Integer status;

    /**
     * 是否缓存：0-不缓存 1-缓存
     */
    private Integer isCache;
    /**
     * id
     */
    private Long id;
    private static final long serialVersionUID = 1L;
}
