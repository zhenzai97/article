package com.springbootinit.model.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 首页区块 VO（管理端列表）
 */
@Data
public class HomeSectionVO implements Serializable {

    private Long id;

    private String code;

    private Integer visible;

    private Integer sort;

    private Integer limitNum;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
