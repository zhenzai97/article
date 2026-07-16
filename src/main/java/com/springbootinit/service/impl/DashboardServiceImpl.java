package com.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springbootinit.model.entity.Advertising;
import com.springbootinit.model.entity.AdvertisingSpace;
import com.springbootinit.model.entity.Article;
import com.springbootinit.model.entity.ArticleCat;
import com.springbootinit.model.entity.TourismContent;
import com.springbootinit.model.vo.DashboardOverviewVO;
import com.springbootinit.model.vo.DashboardOverviewVO.DashboardAdvertisingItemVO;
import com.springbootinit.model.vo.DashboardOverviewVO.DashboardArticleItemVO;
import com.springbootinit.model.vo.DashboardOverviewVO.DashboardCategoryItemVO;
import com.springbootinit.model.vo.DashboardOverviewVO.DashboardStatsVO;
import com.springbootinit.model.vo.DashboardOverviewVO.DashboardTourismItemVO;
import com.springbootinit.model.vo.DashboardOverviewVO.DashboardTourismTypeCountVO;
import com.springbootinit.model.vo.DashboardOverviewVO.DashboardTrendItemVO;
import com.springbootinit.service.AdvertisingService;
import com.springbootinit.service.AdvertisingSpaceService;
import com.springbootinit.service.ArticleCatService;
import com.springbootinit.service.ArticleService;
import com.springbootinit.service.DashboardService;
import com.springbootinit.service.TourismContentService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 工作台概览服务实现
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    private static final int RECENT_LIMIT = 6;

    @Resource
    private ArticleService articleService;

    @Resource
    private ArticleCatService articleCatService;

    @Resource
    private TourismContentService tourismContentService;

    @Resource
    private AdvertisingService advertisingService;

    @Resource
    private AdvertisingSpaceService advertisingSpaceService;

    @Override
    public DashboardOverviewVO getOverview() {
        DashboardOverviewVO vo = new DashboardOverviewVO();
        vo.setStats(buildStats());
        vo.setRecentArticles(buildRecentArticles());
        vo.setRecentTourism(buildRecentTourism());
        vo.setRecentAdvertising(buildRecentAdvertising());
        vo.setWeekTrend(buildWeekTrend());
        vo.setCategories(buildCategories());
        return vo;
    }

    private DashboardStatsVO buildStats() {
        DashboardStatsVO stats = new DashboardStatsVO();
        stats.setArticleTotal(articleService.count());
        stats.setArticleEnabled(articleService.count(new QueryWrapper<Article>().eq("status", 1)));
        stats.setArticleDisabled(articleService.count(new QueryWrapper<Article>().eq("status", 0)));
        stats.setTourismTotal(tourismContentService.count());
        stats.setTourismEnabled(tourismContentService.count(new QueryWrapper<TourismContent>().eq("status", 1)));
        stats.setTourismByType(buildTourismByType());
        return stats;
    }

    private List<DashboardTourismTypeCountVO> buildTourismByType() {
        String[][] typeDefs = {
                {"product", "农产品"},
                {"brand", "品牌"},
                {"market", "市场"},
                {"jewelry", "珠宝玉石"},
                {"food", "美食"},
                {"scenic", "景点"}
        };
        List<TourismContent> list = tourismContentService.list(new QueryWrapper<TourismContent>()
                .select("id", "type"));
        Map<String, Long> countMap = list.stream()
                .filter(item -> item.getType() != null)
                .collect(Collectors.groupingBy(TourismContent::getType, Collectors.counting()));
        List<DashboardTourismTypeCountVO> result = new ArrayList<>();
        for (String[] def : typeDefs) {
            DashboardTourismTypeCountVO item = new DashboardTourismTypeCountVO();
            item.setType(def[0]);
            item.setLabel(def[1]);
            item.setCount(countMap.getOrDefault(def[0], 0L));
            result.add(item);
        }
        return result;
    }

    private List<DashboardArticleItemVO> buildRecentArticles() {
        List<Article> list = articleService.list(new QueryWrapper<Article>()
                .orderByDesc("updateTime")
                .last("LIMIT " + RECENT_LIMIT));
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        Set<Long> categoryIds = list.stream()
                .map(Article::getCategoryId)
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toSet());
        Map<Long, String> categoryNameMap = CollUtil.isEmpty(categoryIds)
                ? new HashMap<>()
                : articleCatService.listByIds(categoryIds).stream()
                        .collect(Collectors.toMap(ArticleCat::getId, ArticleCat::getName, (a, b) -> a));
        return list.stream().map(article -> {
            DashboardArticleItemVO item = new DashboardArticleItemVO();
            item.setId(article.getId());
            item.setTitle(article.getTitle());
            item.setCategoryName(categoryNameMap.getOrDefault(article.getCategoryId(), "-"));
            item.setStatus(article.getStatus());
            item.setReadNum(article.getReadNum() == null ? 0 : article.getReadNum());
            item.setUpdateTime(article.getUpdateTime());
            return item;
        }).collect(Collectors.toList());
    }

    private List<DashboardTourismItemVO> buildRecentTourism() {
        List<TourismContent> list = tourismContentService.list(new QueryWrapper<TourismContent>()
                .orderByDesc("updateTime")
                .last("LIMIT " + RECENT_LIMIT));
        return list.stream().map(row -> {
            DashboardTourismItemVO item = new DashboardTourismItemVO();
            item.setId(row.getId());
            item.setName(row.getName());
            item.setType(row.getType());
            item.setStatus(row.getStatus());
            item.setUpdateTime(row.getUpdateTime());
            return item;
        }).collect(Collectors.toList());
    }

    private List<DashboardAdvertisingItemVO> buildRecentAdvertising() {
        List<Advertising> list = advertisingService.list(new QueryWrapper<Advertising>()
                .orderByDesc("updateTime")
                .last("LIMIT " + RECENT_LIMIT));
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        Set<Long> spaceIds = list.stream()
                .map(Advertising::getSpaceId)
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toSet());
        Map<Long, String> spaceNameMap = CollUtil.isEmpty(spaceIds)
                ? new HashMap<>()
                : advertisingSpaceService.listByIds(spaceIds).stream()
                        .collect(Collectors.toMap(AdvertisingSpace::getId, AdvertisingSpace::getName, (a, b) -> a));
        return list.stream().map(row -> {
            DashboardAdvertisingItemVO item = new DashboardAdvertisingItemVO();
            item.setId(row.getId());
            item.setName(row.getName());
            item.setSpaceName(spaceNameMap.getOrDefault(row.getSpaceId(), "-"));
            item.setStatus(row.getStatus());
            item.setStartTime(row.getStartTime());
            item.setEndTime(row.getEndTime());
            item.setUpdateTime(row.getUpdateTime());
            return item;
        }).collect(Collectors.toList());
    }

    private List<DashboardTrendItemVO> buildWeekTrend() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        Date start = calendar.getTime();

        List<Article> articles = articleService.list(new QueryWrapper<Article>()
                .ge("createTime", start)
                .select("id", "createTime"));

        SimpleDateFormat dayFormat = new SimpleDateFormat("MM-dd");
        Map<String, Long> countMap = new HashMap<>();
        for (Article article : articles) {
            if (article.getCreateTime() == null) {
                continue;
            }
            String key = dayFormat.format(article.getCreateTime());
            countMap.put(key, countMap.getOrDefault(key, 0L) + 1);
        }

        List<DashboardTrendItemVO> trend = new ArrayList<>();
        Calendar cursor = Calendar.getInstance();
        cursor.setTime(start);
        for (int i = 0; i < 7; i++) {
            String label = dayFormat.format(cursor.getTime());
            DashboardTrendItemVO item = new DashboardTrendItemVO();
            item.setLabel(label);
            item.setValue(countMap.getOrDefault(label, 0L));
            trend.add(item);
            cursor.add(Calendar.DAY_OF_MONTH, 1);
        }
        return trend;
    }

    private List<DashboardCategoryItemVO> buildCategories() {
        List<ArticleCat> cats = articleCatService.list(new QueryWrapper<ArticleCat>()
                .orderByAsc("sort")
                .orderByAsc("id"));
        List<Article> articles = articleService.list(new QueryWrapper<Article>()
                .select("id", "categoryId"));
        Map<Long, Long> countMap = articles.stream()
                .filter(a -> a.getCategoryId() != null && a.getCategoryId() > 0)
                .collect(Collectors.groupingBy(Article::getCategoryId, Collectors.counting()));
        long uncategorized = articles.stream()
                .filter(a -> a.getCategoryId() == null || a.getCategoryId() <= 0)
                .count();
        long total = articles.size();

        List<DashboardCategoryItemVO> result = new ArrayList<>();
        for (ArticleCat cat : cats) {
            long count = countMap.getOrDefault(cat.getId(), 0L);
            DashboardCategoryItemVO item = new DashboardCategoryItemVO();
            item.setId(cat.getId());
            item.setName(cat.getName());
            item.setCount(count);
            item.setPercent(total == 0 ? 0 : (int) Math.round(count * 100.0 / total));
            result.add(item);
        }
        if (uncategorized > 0) {
            DashboardCategoryItemVO other = new DashboardCategoryItemVO();
            other.setId(null);
            other.setName("未分类");
            other.setCount(uncategorized);
            other.setPercent(total == 0 ? 0 : (int) Math.round(uncategorized * 100.0 / total));
            result.add(other);
        }
        return result;
    }
}
