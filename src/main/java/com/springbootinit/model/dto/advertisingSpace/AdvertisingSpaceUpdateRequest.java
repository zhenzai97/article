package com.springbootinit.model.dto.advertisingSpace;
import lombok.Data;

import java.io.Serializable;

/**
 * 运营广告位
 *
 */
@Data
public class AdvertisingSpaceUpdateRequest implements Serializable {
    /**
     * id
     */
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


    private static final long serialVersionUID = 1L;
}
