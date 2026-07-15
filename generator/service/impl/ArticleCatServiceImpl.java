package com.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.constant.CommonConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.mapper.ArticleCatMapper;
import com.springbootinit.model.dto.articleCat.ArticleCatQueryRequest;
import com.springbootinit.model.entity.ArticleCat;
import com.springbootinit.model.vo.ArticleCatVO;
import com.springbootinit.service.ArticleCatService;
import com.springbootinit.utils.SqlUtils;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 文章分类服务实现
 *
 */
@Service
public class ArticleCatServiceImpl extends ServiceImpl<ArticleCatMapper, ArticleCat> implements ArticleCatService {

    @Override
    public void validArticleCat(ArticleCat articleCat, boolean add) {
        ThrowUtils.throwIf(articleCat == null, ErrorCode.PARAMS_ERROR);
        String sign = articleCat.getSign();
        String name = articleCat.getName();
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(sign, name), ErrorCode.PARAMS_ERROR, "分类标识和名称不能为空");
        }
        if (StringUtils.isNotBlank(sign) && sign.length() > 64) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类标识过长");
        }
        if (StringUtils.isNotBlank(name) && name.length() > 128) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类名称过长");
        }
    }

    @Override
    public QueryWrapper<ArticleCat> getQueryWrapper(ArticleCatQueryRequest articleCatQueryRequest) {
        QueryWrapper<ArticleCat> queryWrapper = new QueryWrapper<>();
        if (articleCatQueryRequest == null) {
            return queryWrapper;
        }
        Long id = articleCatQueryRequest.getId();
        String sign = articleCatQueryRequest.getSign();
        String name = articleCatQueryRequest.getName();
        Integer status = articleCatQueryRequest.getStatus();
        String sortField = articleCatQueryRequest.getSortField();
        String sortOrder = articleCatQueryRequest.getSortOrder();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(sign), "sign", sign);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder), sortField);
        return queryWrapper;
    }

    @Override
    public ArticleCatVO getArticleCatVO(ArticleCat articleCat) {
        if (articleCat == null) {
            return null;
        }
        ArticleCatVO articleCatVO = new ArticleCatVO();
        BeanUtils.copyProperties(articleCat, articleCatVO);
        return articleCatVO;
    }

    @Override
    public Page<ArticleCatVO> getArticleCatVOPage(Page<ArticleCat> articleCatPage) {
        List<ArticleCat> articleCatList = articleCatPage.getRecords();
        Page<ArticleCatVO> articleCatVOPage = new Page<>(articleCatPage.getCurrent(), articleCatPage.getSize(),
                articleCatPage.getTotal());
        if (CollUtil.isEmpty(articleCatList)) {
            return articleCatVOPage;
        }
        List<ArticleCatVO> articleCatVOList = articleCatList.stream().map(this::getArticleCatVO)
                .collect(Collectors.toList());
        articleCatVOPage.setRecords(articleCatVOList);
        return articleCatVOPage;
    }
}
