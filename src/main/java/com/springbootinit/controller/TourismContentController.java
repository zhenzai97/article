package com.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.annotation.AuthCheck;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.constant.UserConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.model.dto.tourism.TourismContentAddRequest;
import com.springbootinit.model.dto.tourism.TourismContentQueryRequest;
import com.springbootinit.model.dto.tourism.TourismContentUpdateRequest;
import com.springbootinit.model.entity.TourismContent;
import com.springbootinit.model.entity.User;
import com.springbootinit.model.vo.TourismContentVO;
import com.springbootinit.service.TourismContentService;
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
 * 文旅内容接口
 */
@RestController
@RequestMapping("/tourismContent")
@Slf4j
public class TourismContentController {

    @Resource
    private TourismContentService tourismContentService;

    @Resource
    private UserService userService;

    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addTourismContent(@RequestBody TourismContentAddRequest addRequest,
            HttpServletRequest request) {
        ThrowUtils.throwIf(addRequest == null, ErrorCode.PARAMS_ERROR);
        TourismContent entity = new TourismContent();
        BeanUtils.copyProperties(addRequest, entity);
        entity.setAlbum(tourismContentService.toAlbumJson(addRequest.getAlbum()));
        tourismContentService.validTourismContent(entity, true);
        User loginUser = userService.getLoginUser(request);
        entity.setCreateUserId(loginUser.getId());
        if (entity.getStatus() == null) {
            entity.setStatus(1);
        }
        if (entity.getSort() == null) {
            entity.setSort(0);
        }
        if (entity.getIsRecommend() == null) {
            entity.setIsRecommend(0);
        }
        if (entity.getReadCount() == null) {
            entity.setReadCount(0);
        }
        boolean result = tourismContentService.save(entity);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(entity.getId());
    }

    @DeleteMapping("/delete/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteTourismContent(@PathVariable("id") long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TourismContent old = tourismContentService.getById(id);
        ThrowUtils.throwIf(old == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = tourismContentService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateTourismContent(
            @RequestBody TourismContentUpdateRequest updateRequest) {
        if (updateRequest == null || updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TourismContent old = tourismContentService.getById(updateRequest.getId());
        ThrowUtils.throwIf(old == null, ErrorCode.NOT_FOUND_ERROR);
        TourismContent entity = new TourismContent();
        BeanUtils.copyProperties(updateRequest, entity);
        entity.setAlbum(tourismContentService.toAlbumJson(updateRequest.getAlbum()));
        tourismContentService.validTourismContent(entity, false);
        boolean result = tourismContentService.updateById(entity);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @GetMapping("/get/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<TourismContentVO> getTourismContentVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        TourismContent entity = tourismContentService.getById(id);
        ThrowUtils.throwIf(entity == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(tourismContentService.getTourismContentVO(entity));
    }

    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<TourismContentVO>> listTourismContentVOByPage(
            @RequestBody TourismContentQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<TourismContent> page = tourismContentService.page(new Page<>(current, size),
                tourismContentService.getQueryWrapper(queryRequest));
        return ResultUtils.success(tourismContentService.getTourismContentVOPage(page));
    }
}
