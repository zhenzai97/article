package com.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.constant.CommonConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.mapper.ArticleMapper;
import com.springbootinit.model.dto.article.ArticleQueryRequest;
import com.springbootinit.model.entity.ArticleCat;
import com.springbootinit.model.entity.Article;
import com.springbootinit.model.vo.ArticleVO;
import com.springbootinit.service.ArticleCatService;
import com.springbootinit.service.ArticleService;
import com.springbootinit.utils.SqlUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 文章服务实现
 *
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleCatService articleCatService;

    @Override
    public void validArticle(Article article, boolean add) {
        ThrowUtils.throwIf(article == null, ErrorCode.PARAMS_ERROR);
        String title = article.getTitle();
        Long categoryId = article.getCategoryId();
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR, "标题不能为空");
            ThrowUtils.throwIf(categoryId == null || categoryId <= 0, ErrorCode.PARAMS_ERROR, "分类不能为空");
        }
        if (StringUtils.isNotBlank(title) && title.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (categoryId != null && categoryId > 0) {
            ArticleCat category = articleCatService.getById(categoryId);
            ThrowUtils.throwIf(category == null, ErrorCode.PARAMS_ERROR, "分类不存在");
        }
    }

    @Override
    public QueryWrapper<Article> getQueryWrapper(ArticleQueryRequest articleQueryRequest) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        if (articleQueryRequest == null) {
            return queryWrapper;
        }
        Long id = articleQueryRequest.getId();
        Long categoryId = articleQueryRequest.getCategoryId();
        String title = articleQueryRequest.getTitle();
        Integer status = articleQueryRequest.getStatus();
        Integer isHome = articleQueryRequest.getIsHome();
        Integer isTop = articleQueryRequest.getIsTop();
        String sortField = articleQueryRequest.getSortField();
        String sortOrder = articleQueryRequest.getSortOrder();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(categoryId != null, "categoryId", categoryId);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.eq(isHome != null, "isHome", isHome);
        queryWrapper.eq(isTop != null, "isTop", isTop);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder), sortField);
        return queryWrapper;
    }

    @Override
    public ArticleVO getArticleVO(Article article) {
        if (article == null) {
            return null;
        }
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        if (article.getCategoryId() != null) {
            ArticleCat category = articleCatService.getById(article.getCategoryId());
            if (category != null) {
                articleVO.setCategoryName(category.getName());
            }
        }
        return articleVO;
    }

    @Override
    public Page<ArticleVO> getArticleVOPage(Page<Article> articlePage) {
        List<Article> articleList = articlePage.getRecords();
        Page<ArticleVO> articleVOPage = new Page<>(articlePage.getCurrent(), articlePage.getSize(),
                articlePage.getTotal());
        if (CollUtil.isEmpty(articleList)) {
            return articleVOPage;
        }
        Set<Long> categoryIdSet = articleList.stream()
                .map(Article::getCategoryId)
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toSet());
        Map<Long, String> categoryNameMap = articleCatService.listByIds(categoryIdSet).stream()
                .collect(Collectors.toMap(ArticleCat::getId, ArticleCat::getName, (a, b) -> a));
        List<ArticleVO> articleVOList = articleList.stream().map(article -> {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article, articleVO);
            if (article.getCategoryId() != null) {
                articleVO.setCategoryName(categoryNameMap.get(article.getCategoryId()));
            }
            return articleVO;
        }).collect(Collectors.toList());
        articleVOPage.setRecords(articleVOList);
        return articleVOPage;
    }
}
