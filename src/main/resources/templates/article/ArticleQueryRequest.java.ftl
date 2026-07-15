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
     * 分类 id
     */
    private Long categoryId;

    /**
     * 标题（模糊）
     */
    private String title;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;

    /**
     * 是否首页展示：0-否 1-是
     */
    private Integer isHome;

    /**
     * 是否置顶：0-否 1-是
     */
    private Integer isTop;

    private static final long serialVersionUID = 1L;
}
