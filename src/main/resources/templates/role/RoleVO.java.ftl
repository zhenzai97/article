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
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否内置：0-否 1-是
     */
    private Integer isBuiltin;

    /**
     * 是否超级管理员：0-否 1-是
     */
    private Integer isSuperAdmin;

    /**
     * 数据范围：1-全部 2-本部门 3-仅本人
     */
    private Integer dataScope;

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
