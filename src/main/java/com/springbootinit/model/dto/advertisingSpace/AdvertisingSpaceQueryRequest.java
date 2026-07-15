package com.springbootinit.model.dto.advertisingSpace;

import com.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
@EqualsAndHashCode(callSuper = true)
@Data
public class AdvertisingSpaceQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题（模糊）
     */
    private String name;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;


    private static final long serialVersionUID = 1L;
}
