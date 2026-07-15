package com.springbootinit.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 运营分类视图
 *
 */

@Data
public class AdvertisingSpaceVO implements Serializable {

    private Long id;

    /**
     * 名称
     * 标记
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

    private static final long serialVersionUID = 1L;
}
