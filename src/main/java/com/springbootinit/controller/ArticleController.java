package com.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.annotation.AuthCheck;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.constant.UserConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.model.dto.article.ArticleAddRequest;
import com.springbootinit.model.dto.article.ArticleQueryRequest;
import com.springbootinit.model.dto.article.ArticleUpdateRequest;
import com.springbootinit.model.entity.Article;
import com.springbootinit.model.entity.User;
import com.springbootinit.model.vo.ArticleVO;
import com.springbootinit.service.ArticleService;
import com.springbootinit.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章接口
 *
 */
@RestController
@RequestMapping("/article")
@Slf4j
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private UserService userService;

    /**
     * 创建文章
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addArticle(@RequestBody ArticleAddRequest articleAddRequest,
            HttpServletRequest request) {
        ThrowUtils.throwIf(articleAddRequest == null, ErrorCode.PARAMS_ERROR);
        Article article = new Article();
        BeanUtils.copyProperties(articleAddRequest, article);
        articleService.validArticle(article, true);
        User loginUser = userService.getLoginUser(request);
        article.setCreateUserId(loginUser.getId());
        if (article.getStatus() == null) {
            article.setStatus(1);
        }
        if (article.getSort() == null) {
            article.setSort(0);
        }
        if (article.getReadNum() == null) {
            article.setReadNum(0);
        }
        if (article.getIsHome() == null) {
            article.setIsHome(0);
        }
        if (article.getIsTop() == null) {
            article.setIsTop(0);
        }
        boolean result = articleService.save(article);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(article.getId());
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/delete/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteArticle(@PathVariable("id") long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Article oldArticle = articleService.getById(id);
        ThrowUtils.throwIf(oldArticle == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = articleService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新文章
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateArticle(@RequestBody ArticleUpdateRequest articleUpdateRequest) {
        if (articleUpdateRequest == null || articleUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Article oldArticle = articleService.getById(articleUpdateRequest.getId());
        ThrowUtils.throwIf(oldArticle == null, ErrorCode.NOT_FOUND_ERROR);
        Article article = new Article();
        BeanUtils.copyProperties(articleUpdateRequest, article);
        articleService.validArticle(article, false);
        boolean result = articleService.updateById(article);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取文章
     */
    @GetMapping("/get/vo")
    public BaseResponse<ArticleVO> getArticleVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        Article article = articleService.getById(id);
        ThrowUtils.throwIf(article == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(articleService.getArticleVO(article));
    }

    /**
     * 分页获取文章列表
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Article>> listArticleByPage(@RequestBody ArticleQueryRequest articleQueryRequest) {
        long current = articleQueryRequest.getCurrent();
        long size = articleQueryRequest.getPageSize();
        Page<Article> articlePage = articleService.page(new Page<>(current, size),
                articleService.getQueryWrapper(articleQueryRequest));
        return ResultUtils.success(articlePage);
    }

    /**
     * 分页获取文章列表（封装类）
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ArticleVO>> listArticleVOByPage(
            @RequestBody ArticleQueryRequest articleQueryRequest) {
        long current = articleQueryRequest.getCurrent();
        long size = articleQueryRequest.getPageSize();
        Page<Article> articlePage = articleService.page(new Page<>(current, size),
                articleService.getQueryWrapper(articleQueryRequest));
        return ResultUtils.success(articleService.getArticleVOPage(articlePage));
    }
}
