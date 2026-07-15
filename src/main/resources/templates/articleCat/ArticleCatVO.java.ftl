package ${packageName}.model.vo;

import java.io.Serializable;
import java.util.Date;
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
     * 分类标识
     */
    private String sign;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

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

    private static final long serialVersionUID = 1L;
}
