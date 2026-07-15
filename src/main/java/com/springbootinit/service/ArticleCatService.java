package com.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springbootinit.model.dto.articleCat.ArticleCatQueryRequest;
import com.springbootinit.model.entity.ArticleCat;
import com.springbootinit.model.vo.ArticleCatVO;
import java.util.List;

/**
 * 文章分类服务
 *
 */
public interface ArticleCatService extends IService<ArticleCat> {

    /**
     * 校验数据
     */
    void validArticleCat(ArticleCat articleCat, boolean add);

    /**
     * 获取查询条件
     */
    QueryWrapper<ArticleCat> getQueryWrapper(ArticleCatQueryRequest articleCatQueryRequest);

    /**
     * 获取文章分类封装
     */
    ArticleCatVO getArticleCatVO(ArticleCat articleCat);

    /**
     * 分页获取文章分类封装
     */
    Page<ArticleCatVO> getArticleCatVOPage(Page<ArticleCat> articleCatPage);



    List<ArticleCatVO> getArticleCatVoAllData(ArticleCatQueryRequest articleCatQueryRequest);
}
