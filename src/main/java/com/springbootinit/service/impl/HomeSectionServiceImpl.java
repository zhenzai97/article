package com.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.mapper.HomeSectionMapper;
import com.springbootinit.model.entity.Advertising;
import com.springbootinit.model.entity.AdvertisingSpace;
import com.springbootinit.model.entity.Article;
import com.springbootinit.model.entity.ArticleCat;
import com.springbootinit.model.entity.HomeSection;
import com.springbootinit.model.vo.HomeIndexVO;
import com.springbootinit.model.vo.HomeIndexVO.HomeAdItemVO;
import com.springbootinit.model.vo.HomeIndexVO.HomeArticleItemVO;
import com.springbootinit.model.vo.HomeIndexVO.HomeSectionMetaVO;
import com.springbootinit.model.vo.HomeSectionVO;
import com.springbootinit.service.AdvertisingService;
import com.springbootinit.service.AdvertisingSpaceService;
import com.springbootinit.service.ArticleCatService;
import com.springbootinit.service.ArticleService;
import com.springbootinit.service.HomeSectionService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 首页区块服务实现
 */
@Service
public class HomeSectionServiceImpl extends ServiceImpl<HomeSectionMapper, HomeSection>
        implements HomeSectionService {

    @Resource
    private AdvertisingSpaceService advertisingSpaceService;

    @Resource
    private AdvertisingService advertisingService;

    @Resource
    private ArticleService articleService;

    @Resource
    private ArticleCatService articleCatService;

    @Override
    public List<HomeSectionVO> listAllVO() {
        List<HomeSection> list = this.list(new QueryWrapper<HomeSection>()
                .orderByDesc("sort")
                .orderByAsc("id"));
        return list.stream().map(this::getVO).collect(Collectors.toList());
    }

    @Override
    public HomeSectionVO getVO(HomeSection section) {
        if (section == null) {
            return null;
        }
        HomeSectionVO vo = new HomeSectionVO();
        BeanUtils.copyProperties(section, vo);
        return vo;
    }

    @Override
    public HomeIndexVO buildHomeIndex() {
        List<HomeSection> sections = this.list(new QueryWrapper<HomeSection>()
                .eq("status", 1)
                .orderByDesc("sort")
                .orderByAsc("id"));

        HomeIndexVO vo = new HomeIndexVO();
        vo.setSections(sections.stream().map(this::toMeta).collect(Collectors.toList()));
        vo.setCarouselList(new ArrayList<>());
        vo.setChannelList(new ArrayList<>());
        vo.setBannerList(new ArrayList<>());
        vo.setSpecialList(new ArrayList<>());
        vo.setRecommendedArticles(new ArrayList<>());
        vo.setWonderfulList(new ArrayList<>());
        vo.setAssociationList(new ArrayList<>());
        vo.setActivityList(Collections.emptyList());

        for (HomeSection section : sections) {
            if (section.getVisible() == null || section.getVisible() != 1) {
                continue;
            }
            int limit = section.getLimitNum() == null || section.getLimitNum() <= 0
                    ? 5 : section.getLimitNum();
            String code = section.getCode();
            if ("carousel".equals(code) || "channel".equals(code)
                    || "banner".equals(code) || "special".equals(code)) {
                List<HomeAdItemVO> ads = listAdsBySpaceSign(section.getSpaceSign(), limit);
                switch (code) {
                    case "carousel":
                        vo.setCarouselList(ads);
                        break;
                    case "channel":
                        vo.setChannelList(ads);
                        break;
                    case "banner":
                        vo.setBannerList(ads);
                        break;
                    case "special":
                        vo.setSpecialList(ads);
                        break;
                    default:
                        break;
                }
            } else if ("article_home".equals(code)) {
                vo.setRecommendedArticles(listHomeArticles(limit));
            } else if (StringUtils.isNotBlank(section.getCategorySign())) {
                List<HomeArticleItemVO> articles = listArticlesByCategorySign(
                        section.getCategorySign(), limit, true);
                if ("dhnews".equals(section.getCategorySign()) || code.contains("dhnews")) {
                    vo.setWonderfulList(articles);
                } else if ("xhdt".equals(section.getCategorySign()) || code.contains("xhdt")) {
                    vo.setAssociationList(articles);
                } else if (CollUtil.isEmpty(vo.getWonderfulList())) {
                    // 兜底：第一个栏目型区块
                    vo.setWonderfulList(articles);
                } else {
                    vo.setAssociationList(articles);
                }
            }
            // activity：暂无业务表，保持空列表
        }
        return vo;
    }

    private HomeSectionMetaVO toMeta(HomeSection section) {
        HomeSectionMetaVO meta = new HomeSectionMetaVO();
        meta.setCode(section.getCode());
        meta.setTitle(section.getTitle());
        meta.setVisible(section.getVisible());
        meta.setSort(section.getSort());
        meta.setLimitNum(section.getLimitNum());
        meta.setSpaceSign(section.getSpaceSign());
        meta.setCategorySign(section.getCategorySign());
        return meta;
    }

    private List<HomeAdItemVO> listAdsBySpaceSign(String spaceSign, int limit) {
        if (StringUtils.isBlank(spaceSign)) {
            return new ArrayList<>();
        }
        AdvertisingSpace space = advertisingSpaceService.getOne(new QueryWrapper<AdvertisingSpace>()
                .eq("sign", spaceSign)
                .eq("status", 1)
                .last("LIMIT 1"));
        if (space == null) {
            return new ArrayList<>();
        }
        Date now = new Date();
        QueryWrapper<Advertising> qw = new QueryWrapper<Advertising>()
                .eq("spaceId", space.getId())
                .eq("status", 1)
                .and(w -> w.isNull("startTime").or().le("startTime", now))
                .and(w -> w.isNull("endTime").or().ge("endTime", now))
                .orderByDesc("sort")
                .orderByDesc("updateTime")
                .last("LIMIT " + limit);
        return advertisingService.list(qw).stream().map(ad -> {
            HomeAdItemVO item = new HomeAdItemVO();
            item.setId(ad.getId());
            item.setName(ad.getName());
            item.setCover(ad.getCover());
            item.setPath(ad.getPath());
            item.setVideo(ad.getVideo());
            item.setRemark(ad.getRemark());
            item.setSort(ad.getSort());
            return item;
        }).collect(Collectors.toList());
    }

    private List<HomeArticleItemVO> listHomeArticles(int limit) {
        List<Article> list = articleService.list(new QueryWrapper<Article>()
                .eq("status", 1)
                .eq("isHome", 1)
                .orderByDesc("isTop")
                .orderByDesc("sort")
                .orderByDesc("updateTime")
                .last("LIMIT " + limit));
        return toArticleItems(list);
    }

    private List<HomeArticleItemVO> listArticlesByCategorySign(String sign, int limit,
            boolean excludeHome) {
        ArticleCat cat = articleCatService.getOne(new QueryWrapper<ArticleCat>()
                .eq("sign", sign)
                .eq("status", 1)
                .last("LIMIT 1"));
        if (cat == null) {
            return new ArrayList<>();
        }
        QueryWrapper<Article> qw = new QueryWrapper<Article>()
                .eq("status", 1)
                .eq("categoryId", cat.getId());
        if (excludeHome) {
            qw.and(w -> w.isNull("isHome").or().eq("isHome", 0));
        }
        qw.orderByDesc("isTop").orderByDesc("sort").orderByDesc("updateTime")
                .last("LIMIT " + limit);
        List<HomeArticleItemVO> items = toArticleItems(articleService.list(qw));
        items.forEach(item -> {
            item.setCategorySign(sign);
            item.setCategoryName(cat.getName());
        });
        return items;
    }

    private List<HomeArticleItemVO> toArticleItems(List<Article> list) {
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        Map<Long, ArticleCat> catMap = new HashMap<>();
        List<Long> catIds = list.stream()
                .map(Article::getCategoryId)
                .filter(id -> id != null && id > 0)
                .distinct()
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(catIds)) {
            catMap = articleCatService.listByIds(catIds).stream()
                    .collect(Collectors.toMap(ArticleCat::getId, c -> c, (a, b) -> a));
        }
        Map<Long, ArticleCat> finalCatMap = catMap;
        return list.stream().map(article -> {
            HomeArticleItemVO item = new HomeArticleItemVO();
            item.setId(article.getId());
            item.setTitle(article.getTitle());
            item.setCover(article.getCover());
            item.setVideo(article.getVideo());
            item.setReadNum(article.getReadNum() == null ? 0 : article.getReadNum());
            item.setIsHome(article.getIsHome());
            item.setCreateTime(article.getCreateTime());
            ArticleCat cat = finalCatMap.get(article.getCategoryId());
            if (cat != null) {
                item.setCategorySign(cat.getSign());
                item.setCategoryName(cat.getName());
            }
            return item;
        }).collect(Collectors.toList());
    }
}
