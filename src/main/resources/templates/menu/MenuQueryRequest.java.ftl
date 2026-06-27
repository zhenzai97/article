package ${packageName}.model.dto.${dataKey};

import ${packageName}.common.PageRequest;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询${dataName}请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ${upperDataKey}QueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 父菜单 id
     */
    private Long parentId;

    /**
     * 菜单名称（模糊）
     */
    private String menuName;

    /**
     * 菜单类型：1-目录 2-菜单 3-按钮
     */
    private Integer menuType;

    /**
     * 显示状态
     */
    private Integer visible;

    /**
     * 菜单状态
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
