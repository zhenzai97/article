package com.springbootinit.model.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 文旅内容视图
 */
@Data
public class TourismContentVO implements Serializable {

    private Long id;

    private String type;

    private String name;

      private String cover;


    /**
     * 营业执照
     */
    private String license;

    private String intro;

    private String content;

    private Integer sort;

    private Integer status;

    private Integer isRecommend;

    private Integer readCount;

    /** 相册图片列表 */
    private List<String> album;

    private String address;

    private String coordinate;

    private String mobile;

    private String openTime;

    private String ticketPrice;

    private String sourceId;

    private Long createUserId;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
