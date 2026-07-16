package com.springbootinit.model.vo;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 工作台概览
 */
@Data
public class DashboardOverviewVO implements Serializable {

    private DashboardStatsVO stats;

    private List<DashboardArticleItemVO> recentArticles;

    private List<DashboardTourismItemVO> recentTourism;

    private List<DashboardAdvertisingItemVO> recentAdvertising;

    /** 近 7 日文章新增 */
    private List<DashboardTrendItemVO> weekTrend;

    /** 文章栏目占比 */
    private List<DashboardCategoryItemVO> categories;

    private static final long serialVersionUID = 1L;

    @Data
    public static class DashboardStatsVO implements Serializable {
        private long articleTotal;
        private long articleEnabled;
        private long articleDisabled;
        private long tourismTotal;
        private long tourismEnabled;
        /** 文旅各类型数量 */
        private List<DashboardTourismTypeCountVO> tourismByType;
        private static final long serialVersionUID = 1L;
    }

    @Data
    public static class DashboardTourismTypeCountVO implements Serializable {
        private String type;
        private String label;
        private long count;
        private static final long serialVersionUID = 1L;
    }

    @Data
    public static class DashboardArticleItemVO implements Serializable {
        private Long id;
        private String title;
        private String categoryName;
        private Integer status;
        private Integer readNum;
        private java.util.Date updateTime;
        private static final long serialVersionUID = 1L;
    }

    @Data
    public static class DashboardTourismItemVO implements Serializable {
        private Long id;
        private String name;
        private String type;
        private Integer status;
        private java.util.Date updateTime;
        private static final long serialVersionUID = 1L;
    }

    @Data
    public static class DashboardAdvertisingItemVO implements Serializable {
        private Long id;
        private String name;
        private String spaceName;
        private Integer status;
        private java.util.Date startTime;
        private java.util.Date endTime;
        private java.util.Date updateTime;
        private static final long serialVersionUID = 1L;
    }

    @Data
    public static class DashboardTrendItemVO implements Serializable {
        private String label;
        private long value;
        private static final long serialVersionUID = 1L;
    }

    @Data
    public static class DashboardCategoryItemVO implements Serializable {
        private String name;
        private long count;
        private int percent;
        private static final long serialVersionUID = 1L;
    }
}
