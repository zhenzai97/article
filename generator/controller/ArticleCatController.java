package com.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.annotation.AuthCheck;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.constant.UserConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.model.dto.articleCat.ArticleCatAddRequest;
import com.springbootinit.model.dto.articleCat.ArticleCatQueryRequest;
import com.springbootinit.model.dto.articleCat.ArticleCatUpdateRequest;
import com.springbootinit.model.entity.ArticleCat;
import com.springbootinit.model.entity.User;
import com.springbootinit.model.vo.ArticleCatVO;
import com.springbootinit.service.ArticleCatService;
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
 * 文章分类接口
 *
 */
@RestController
@RequestMapping("/articleCat")
@Slf4j
public class ArticleCatController {

    @Resource
    private ArticleCatService articleCatService;

    @Resource
    private UserService userService;

    /**
     * 创建文章分类
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addArticleCat(@RequestBody ArticleCatAddRequest articleCatAddRequest,
            HttpServletRequest request) {
        ThrowUtils.throwIf(articleCatAddRequest == null, ErrorCode.PARAMS_ERROR);
        ArticleCat articleCat = new ArticleCat();
        BeanUtils.copyProperties(articleCatAddRequest, articleCat);
        articleCatService.validArticleCat(articleCat, true);
        User loginUser = userService.getLoginUser(request);
        articleCat.setCreateUserId(loginUser.getId());
        if (articleCat.getStatus() == null) {
            articleCat.setStatus(1);
        }
        if (articleCat.getSort() == null) {
            articleCat.setSort(0);
        }
        boolean result = articleCatService.save(articleCat);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(articleCat.getId());
    }

    /**
     * 删除文章分类
     */
    @DeleteMapping("/delete/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteArticleCat(@PathVariable("id") long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ArticleCat oldArticleCat = articleCatService.getById(id);
        ThrowUtils.throwIf(oldArticleCat == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = articleCatService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新文章分类
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateArticleCat(@RequestBody ArticleCatUpdateRequest articleCatUpdateRequest) {
        if (articleCatUpdateRequest == null || articleCatUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ArticleCat oldArticleCat = articleCatService.getById(articleCatUpdateRequest.getId());
        ThrowUtils.throwIf(oldArticleCat == null, ErrorCode.NOT_FOUND_ERROR);
        ArticleCat articleCat = new ArticleCat();
        BeanUtils.copyProperties(articleCatUpdateRequest, articleCat);
        articleCatService.validArticleCat(articleCat, false);
        boolean result = articleCatService.updateById(articleCat);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取文章分类
     */
    @GetMapping("/get/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<ArticleCatVO> getArticleCatVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        ArticleCat articleCat = articleCatService.getById(id);
        ThrowUtils.throwIf(articleCat == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(articleCatService.getArticleCatVO(articleCat));
    }

    /**
     * 分页获取文章分类列表
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<ArticleCat>> listArticleCatByPage(@RequestBody ArticleCatQueryRequest articleCatQueryRequest) {
        long current = articleCatQueryRequest.getCurrent();
        long size = articleCatQueryRequest.getPageSize();
        Page<ArticleCat> articleCatPage = articleCatService.page(new Page<>(current, size),
                articleCatService.getQueryWrapper(articleCatQueryRequest));
        return ResultUtils.success(articleCatPage);
    }

    /**
     * 分页获取文章分类列表（封装类）
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<ArticleCatVO>> listArticleCatVOByPage(
            @RequestBody ArticleCatQueryRequest articleCatQueryRequest) {
        long current = articleCatQueryRequest.getCurrent();
        long size = articleCatQueryRequest.getPageSize();
        Page<ArticleCat> articleCatPage = articleCatService.page(new Page<>(current, size),
                articleCatService.getQueryWrapper(articleCatQueryRequest));
        return ResultUtils.success(articleCatService.getArticleCatVOPage(articleCatPage));
    }
}
