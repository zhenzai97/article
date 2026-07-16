package com.springbootinit.controller;

import com.springbootinit.annotation.AuthCheck;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.constant.UserConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.model.dto.home.HomeSectionUpdateRequest;
import com.springbootinit.model.entity.HomeSection;
import com.springbootinit.model.vo.HomeSectionVO;
import com.springbootinit.service.HomeSectionService;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页区块配置（管理端）
 */
@RestController
@RequestMapping("/homeSection")
@Slf4j
public class HomeSectionController {

    @Resource
    private HomeSectionService homeSectionService;

    @GetMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<HomeSectionVO>> list() {
        return ResultUtils.success(homeSectionService.listAllVO());
    }

    /**
     * 仅更新显隐 / 排序 / 条数；title、spaceSign、categorySign 由种子数据维护，不允许改
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> update(@RequestBody HomeSectionUpdateRequest request) {
        if (request == null || request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        HomeSection old = homeSectionService.getById(request.getId());
        ThrowUtils.throwIf(old == null, ErrorCode.NOT_FOUND_ERROR);
        HomeSection entity = new HomeSection();
        entity.setId(request.getId());
        if (request.getVisible() != null) {
            entity.setVisible(request.getVisible());
            // 显示与状态合一
            entity.setStatus(request.getVisible());
        }
        if (request.getSort() != null) {
            entity.setSort(request.getSort());
        }
        if (request.getLimitNum() != null) {
            ThrowUtils.throwIf(request.getLimitNum() < 1 || request.getLimitNum() > 50,
                    ErrorCode.PARAMS_ERROR, "条数需在 1~50");
            entity.setLimitNum(request.getLimitNum());
        }
        boolean result = homeSectionService.updateById(entity);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
}
