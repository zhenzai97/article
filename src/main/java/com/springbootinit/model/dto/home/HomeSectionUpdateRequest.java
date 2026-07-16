package com.springbootinit.model.dto.home;

import java.io.Serializable;
import lombok.Data;

/**
 * 更新首页区块（管理端仅支持显隐 / 排序 / 条数）
 */
@Data
public class HomeSectionUpdateRequest implements Serializable {

    private Long id;

    /** 是否显示（与 status 同步） */
    private Integer visible;

    private Integer sort;

    private Integer limitNum;

    private static final long serialVersionUID = 1L;
}
