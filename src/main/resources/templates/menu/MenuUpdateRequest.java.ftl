package ${packageName}.model.dto.${dataKey};

import java.io.Serializable;
import lombok.Data;

/**
 * 更新${dataName}请求
 *
 */
@Data
public class ${upperDataKey}UpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 父菜单 id
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

    private static final long serialVersionUID = 1L;
}
