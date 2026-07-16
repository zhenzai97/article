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
 * 文旅内容展示
 * <p>
 * type: product/brand/market/jewelry/food/scenic
 */
@TableName(value = "tourism_content")
@Data
public class TourismContent implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 类型 */
    private String type;

    /** 名称 */
    private String name;

    /** 封面图 */
    private String cover;

    /** 简介 */
    private String intro;

    /** 详情富文本 */
    private String content;

    /** 排序 */
    private Integer sort;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    /** 是否推荐：0-否 1-是 */
    private Integer isRecommend;

    /** 阅读量 */
    private Integer readCount;

    /** 相册 JSON 数组字符串 */
    private String album;

    /** 地址（景点） */
    private String address;

    /** 坐标（景点） */
    private String coordinate;

    /** 联系电话（景点） */
    private String mobile;

    /** 开放时间（景点） */
    private String openTime;

    /** 门票价格（景点） */
    private String ticketPrice;

    /** 旧 CMS _id */
    private String sourceId;

    private Long createUserId;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
