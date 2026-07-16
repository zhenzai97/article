package com.springbootinit.model.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * C 端首页聚合
 */
@Data
public class HomeIndexVO implements Serializable {

    /** 区块编排元数据（按 sort 降序） */
    private List<HomeSectionMetaVO> sections;

    private List<HomeAdItemVO> carouselList;

    private List<HomeAdItemVO> channelList;

    private List<HomeAdItemVO> bannerList;

    private List<HomeAdItemVO> specialList;

    private List<HomeArticleItemVO> recommendedArticles;

    private List<HomeArticleItemVO> wonderfulList;

    private List<HomeArticleItemVO> associationList;

    /** 活动暂无表，固定空列表 */
    private List<Object> activityList;

    private static final long serialVersionUID = 1L;

    @Data
    public static class HomeSectionMetaVO implements Serializable {
        private String code;
        private String title;
        private Integer visible;
        private Integer sort;
        private Integer limitNum;
        private String spaceSign;
        private String categorySign;
        private static final long serialVersionUID = 1L;
    }

    @Data
    public static class HomeAdItemVO implements Serializable {
        private Long id;
        private String name;
        private String cover;
        private String path;
        private String video;
        /** 对应旧 desc */
        private String remark;
        private Integer sort;
        private static final long serialVersionUID = 1L;
    }

    @Data
    public static class HomeArticleItemVO implements Serializable {
        private Long id;
        private String title;
        private String cover;
        private String video;
        private Integer readNum;
        private Integer isHome;
        private String categorySign;
        private String categoryName;
        private Date createTime;
        private static final long serialVersionUID = 1L;
    }
}
