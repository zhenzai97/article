package com.springbootinit.model.dto.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyQueryRequest extends PageRequest implements Serializable {


    /**
     * id
     */
    private Long id;



    /**
     * 公司名称
     */
    private String name;

    /**
     * 公司简称
     */
    private String nickname;

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
     * 公司标签（ 1 抖音 ,2 快手 ,3 微博 ,4 红木 ,  5 大学生 ，6 主播 ，7策划）
     */
    private String tag;

    /**
     * 创建时间范围-开始
     */
    @JsonProperty("cStartTime")
    private String cStartTime;

    /**
     * 创建时间范围-结束
     */
    @JsonProperty("cEndTime")
    private String cEndTime;

    /**
     * 更新时间范围-开始
     */
    @JsonProperty("upStartTime")
    private String upStartTime;

    /**
     * 更新时间范围-结束
     */
    @JsonProperty("upEndTime")
    private String upEndTime;
}
