package ${packageName}.model.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * ${dataName}视图
 *
 */
@Data
public class ${upperDataKey}VO implements Serializable {

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
     * 显示状态
     */
    private Integer visible;

    /**
     * 菜单状态
     */
    private Integer status;

    /**
     * 是否缓存
     */
    private Integer isCache;

    /**
     * 创建人 id
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 子菜单（树形结构）
     */
    private List<${upperDataKey}VO> children;

    private static final long serialVersionUID = 1L;
}
