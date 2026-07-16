package com.springbootinit.model.dto.tourism;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 创建文旅内容请求
 */
@Data
public class TourismContentAddRequest implements Serializable {

    private String type;

    private String name;

    private String cover;

    private String intro;

    private String content;

    private Integer sort;

    private Integer status;

    private Integer isRecommend;

    private Integer readCount;

    private List<String> album;

    private String address;

    private String coordinate;

    private String mobile;

    private String openTime;

    private String ticketPrice;

    private static final long serialVersionUID = 1L;
}
