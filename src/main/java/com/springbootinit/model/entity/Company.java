package com.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Company implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 公司名称
     */
    private String
            name;

    /**
     * 公司简称
     */
    private String nickname;

    /**
     * 公司封面
     */
      private String cover;

    /**
     * 营业执照
     */
    private String license;


    /**
     * 公司介绍
     */
    private String intro;


    /**
     * 会员属性（1会长单位  2副会长单位 3理事单位  4会员单位  5个人单位， 0其它，）
     */
    private Integer vip;


    /**
     * 会员类型（1单位会员  2个人会员）
     */
    private Integer vipType;


    /**
     * 政治面貌（1 共产党员  2 共青团员  3 群众  4其它 ）
     */
    private Integer identity ;


    /**
     * 审核状态（0 待审核  1已通过  2已拒绝）
     */
    private Integer examineStatus ;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;


    /**
     * 联系电话
     */
    private String mobile;


    /**
     * 邮箱
     */
    private String email;


    /**
     * 联系地址
     */
    private String address;

    /**
     * 公司地理坐标
     */
    private String coordinate;

    /**
     * 公司业务
     */
    private String business;

    /**
     * 公司标签（ 1 抖音 ,2 快手 ,3 微博 ,4 红木 ,  5 大学生 ，6 主播 ，7策划）
     */
    private String tag;

    /**
     * 附件
     */
    private String annexe;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 公司招聘介绍
     */
    private String recruitmentDes;

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
}
