package com.springbootinit.model.dto.advertising;

import com.springbootinit.common.PageRequest;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 运营广告查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdvertisingQueryRequest extends PageRequest implements Serializable {

    private Long id;

    /** 运营位 id */
    private Long spaceId;

    /** 标题（模糊） */
    private String name;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
