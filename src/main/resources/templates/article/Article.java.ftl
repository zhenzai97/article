package ${packageName}.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ${dataName}
 *
 */
@TableName(value = "${tableName}")
@Data
public class ${upperDataKey} implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 分类 id
     */
    private Long categoryId;

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

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
