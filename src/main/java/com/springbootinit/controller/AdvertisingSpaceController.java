package com.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.annotation.AuthCheck;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.constant.UserConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.model.dto.advertisingSpace.AdvertisingSpaceAddRequest;
import com.springbootinit.model.dto.advertisingSpace.AdvertisingSpaceQueryRequest;
import com.springbootinit.model.dto.advertisingSpace.AdvertisingSpaceUpdateRequest;
import com.springbootinit.model.entity.AdvertisingSpace;
import com.springbootinit.model.entity.User;
import com.springbootinit.model.vo.AdvertisingSpaceVO;
import com.springbootinit.service.AdvertisingSpaceService;
import com.springbootinit.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
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

@RestController
@RequestMapping("/advertisingSpace")
@Slf4j
public class AdvertisingSpaceController {

    @Resource
    private AdvertisingSpaceService advertisingSpaceService;

    @Resource
    private UserService userService;

    /**
     * 创建运营位
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addAdvertisingSpace(@RequestBody AdvertisingSpaceAddRequest advertisingSpaceAddRequest,
                                                  HttpServletRequest request) {
        ThrowUtils.throwIf(advertisingSpaceAddRequest == null, ErrorCode.PARAMS_ERROR);
        AdvertisingSpace advertisingSpace = new AdvertisingSpace();
        BeanUtils.copyProperties(advertisingSpaceAddRequest, advertisingSpace);
        advertisingSpaceService.validAdvertisingSpace(advertisingSpace, true);
        User loginUser = userService.getLoginUser(request);
        advertisingSpace.setCreateUserId(loginUser.getId());
        if (advertisingSpace.getStatus() == null) {
            advertisingSpace.setStatus(1);
        }
        boolean result = advertisingSpaceService.save(advertisingSpace);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(advertisingSpace.getId());
    }

    /**
     * 删除运营位
     */
    @DeleteMapping("/delete/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteAdvertisingSpace(@PathVariable("id") long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AdvertisingSpace advertisingSpace = advertisingSpaceService.getById(id);
        ThrowUtils.throwIf(advertisingSpace == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = advertisingSpaceService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新运营位
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateAdvertisingSpace(
            @RequestBody AdvertisingSpaceUpdateRequest advertisingSpaceUpdateRequest) {
        if (advertisingSpaceUpdateRequest == null || advertisingSpaceUpdateRequest.getId() == null
                || advertisingSpaceUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AdvertisingSpace oldAdvertisingSpace = advertisingSpaceService.getById(advertisingSpaceUpdateRequest.getId());
        ThrowUtils.throwIf(oldAdvertisingSpace == null, ErrorCode.NOT_FOUND_ERROR);
        AdvertisingSpace advertisingSpace = new AdvertisingSpace();
        BeanUtils.copyProperties(advertisingSpaceUpdateRequest, advertisingSpace);
        advertisingSpaceService.validAdvertisingSpace(advertisingSpace, false);
        boolean result = advertisingSpaceService.updateById(advertisingSpace);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 分页获取运营位
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<AdvertisingSpaceVO>> listAdvertisingSpaceVOByPage(
            @RequestBody AdvertisingSpaceQueryRequest advertisingSpaceQueryRequest) {
        long current = advertisingSpaceQueryRequest.getCurrent();
        long size = advertisingSpaceQueryRequest.getPageSize();
        Page<AdvertisingSpace> advertisingSpacePage = advertisingSpaceService.page(new Page<>(current, size),
                advertisingSpaceService.getQueryWrapper(advertisingSpaceQueryRequest));
        return ResultUtils.success(advertisingSpaceService.getAdvertisingSpaceVOPage(advertisingSpacePage));
    }

    /**
     * 获取全部启用运营位（下拉用）
     */
    @GetMapping("/list/allData")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<AdvertisingSpaceVO>> listAllAdvertisingSpace() {
        List<AdvertisingSpace> list = advertisingSpaceService.list(
                new QueryWrapper<AdvertisingSpace>()
                        .eq("status", 1)
                        .orderByDesc("updateTime"));
        List<AdvertisingSpaceVO> voList = list.stream()
                .map(advertisingSpaceService::getAdvertisingSpaceVO)
                .collect(Collectors.toList());
        return ResultUtils.success(voList);
    }
}
