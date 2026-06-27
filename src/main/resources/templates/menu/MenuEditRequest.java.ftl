package ${packageName}.model.dto.${dataKey};

import java.io.Serializable;
import lombok.Data;

/**
 * 编辑${dataName}请求
 *
 */
@Data
public class ${upperDataKey}EditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 菜单名称
     */
    private String menuName;

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
     * 是否缓存
     */
    private Integer isCache;

    private static final long serialVersionUID = 1L;
}
