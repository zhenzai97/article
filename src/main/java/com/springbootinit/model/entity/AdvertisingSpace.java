package com.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 运营广告位
 *
 */
@TableName(value = "advertising_space")
@Data
public class AdvertisingSpace {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    /**
     * 名称
     */
    private String name;

    /**
     * 标记
     */
    private String sign;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;

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
