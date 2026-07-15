package com.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springbootinit.model.dto.article.ArticleQueryRequest;
import com.springbootinit.model.entity.Article;
import com.springbootinit.model.vo.ArticleVO;

/**
 * 文章服务
 *
 */
public interface ArticleService extends IService<Article> {

    void validArticle(Article article, boolean add);

    QueryWrapper<Article> getQueryWrapper(ArticleQueryRequest articleQueryRequest);

    ArticleVO getArticleVO(Article article);

    Page<ArticleVO> getArticleVOPage(Page<Article> articlePage);
}
