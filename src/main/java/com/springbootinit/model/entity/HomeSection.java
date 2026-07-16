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
 * 首页区块配置
 */
@TableName(value = "home_section")
@Data
public class HomeSection implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 区块编码 */
    private String code;

    /** 展示标题 */
    private String title;

    /** 是否显示 */
    private Integer visible;

    /** 排序 */
    private Integer sort;

    /** 拉取条数 */
    private Integer limitNum;

    /** 运营位 sign */
    private String spaceSign;

    /** 栏目 sign */
    private String categorySign;

    /** 状态 */
    private Integer status;

    private Long createUserId;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
