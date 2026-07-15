package com.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 运营广告
 * <p>
 * 对应旧 CMS：name、sort、cover、start_time、end_time、desc→remark、
 * cate_id→spaceId、path、content、video、status
 */
@TableName(value = "advertising")
@Data
public class Advertising implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 运营位 id */
    private Long spaceId;

    /** 广告标题 */
    private String name;

    /** 封面图 */
    private String cover;

    /** 视频地址 */
    private String video;

    /** 跳转路径 */
    private String path;

    /** 详情内容 */
    private String content;

    /** 备注/简介（旧字段 desc） */
    private String remark;

    /** 排序 */
    private Integer sort;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    /** 开始展示日期 */
    private Date startTime;

    /** 结束展示日期 */
    private Date endTime;

    private Long createUserId;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
