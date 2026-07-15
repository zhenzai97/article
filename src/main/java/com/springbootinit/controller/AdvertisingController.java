package com.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.annotation.AuthCheck;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.constant.UserConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.model.dto.advertising.AdvertisingAddRequest;
import com.springbootinit.model.dto.advertising.AdvertisingQueryRequest;
import com.springbootinit.model.dto.advertising.AdvertisingUpdateRequest;
import com.springbootinit.model.entity.Advertising;
import com.springbootinit.model.entity.User;
import com.springbootinit.model.vo.AdvertisingVO;
import com.springbootinit.service.AdvertisingService;
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
 * 运营广告接口
 */
@RestController
@RequestMapping("/advertising")
@Slf4j
public class AdvertisingController {

    @Resource
    private AdvertisingService advertisingService;

    @Resource
    private UserService userService;

    /**
     * 创建运营广告
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addAdvertising(@RequestBody AdvertisingAddRequest advertisingAddRequest,
            HttpServletRequest request) {
        ThrowUtils.throwIf(advertisingAddRequest == null, ErrorCode.PARAMS_ERROR);
        Advertising advertising = new Advertising();
        BeanUtils.copyProperties(advertisingAddRequest, advertising);
        advertisingService.validAdvertising(advertising, true);
        User loginUser = userService.getLoginUser(request);
        advertising.setCreateUserId(loginUser.getId());
        if (advertising.getStatus() == null) {
            advertising.setStatus(1);
        }
        if (advertising.getSort() == null) {
            advertising.setSort(0);
        }
        boolean result = advertisingService.save(advertising);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(advertising.getId());
    }

    /**
     * 删除运营广告
     */
    @DeleteMapping("/delete/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteAdvertising(@PathVariable("id") long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Advertising oldAdvertising = advertisingService.getById(id);
        ThrowUtils.throwIf(oldAdvertising == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = advertisingService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新运营广告
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateAdvertising(@RequestBody AdvertisingUpdateRequest advertisingUpdateRequest) {
        if (advertisingUpdateRequest == null || advertisingUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Advertising oldAdvertising = advertisingService.getById(advertisingUpdateRequest.getId());
        ThrowUtils.throwIf(oldAdvertising == null, ErrorCode.NOT_FOUND_ERROR);
        Advertising advertising = new Advertising();
        BeanUtils.copyProperties(advertisingUpdateRequest, advertising);
        advertisingService.validAdvertising(advertising, false);
        boolean result = advertisingService.updateById(advertising);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取运营广告
     */
    @GetMapping("/get/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<AdvertisingVO> getAdvertisingVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        Advertising advertising = advertisingService.getById(id);
        ThrowUtils.throwIf(advertising == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(advertisingService.getAdvertisingVO(advertising));
    }

    /**
     * 分页获取运营广告
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<AdvertisingVO>> listAdvertisingVOByPage(
            @RequestBody AdvertisingQueryRequest advertisingQueryRequest) {
        long current = advertisingQueryRequest.getCurrent();
        long size = advertisingQueryRequest.getPageSize();
        Page<Advertising> advertisingPage = advertisingService.page(new Page<>(current, size),
                advertisingService.getQueryWrapper(advertisingQueryRequest));
        return ResultUtils.success(advertisingService.getAdvertisingVOPage(advertisingPage));
    }
}
