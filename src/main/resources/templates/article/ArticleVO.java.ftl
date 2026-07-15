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
     * 分类 id
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 封面图
     */
    private String cover;

    /**
     * 视频地址
     */
    private String video;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 阅读量
     */
    private Integer readNum;

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
