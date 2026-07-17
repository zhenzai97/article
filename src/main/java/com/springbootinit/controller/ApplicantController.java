package com.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.model.dto.applicant.ApplicantAddRequest;
import com.springbootinit.model.dto.applicant.ApplicantQueryRequest;
import com.springbootinit.model.dto.applicant.ApplicantUpdateRequest;
import com.springbootinit.model.entity.Applicant;
import com.springbootinit.model.entity.User;
import com.springbootinit.model.vo.ApplicantVO;
import com.springbootinit.service.ApplicantService;
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
 * 应聘列表接口
 */
@RestController
@RequestMapping("/applicant")
@Slf4j
public class ApplicantController {

    @Resource
    private ApplicantService applicantService;

    @Resource
    private UserService userService;

    @PostMapping("/add")
    public BaseResponse<Long> addApplicant(@RequestBody ApplicantAddRequest addRequest,
            HttpServletRequest request) {
        ThrowUtils.throwIf(addRequest == null, ErrorCode.PARAMS_ERROR);
        Applicant applicant = new Applicant();
        BeanUtils.copyProperties(addRequest, applicant);
        applicantService.fillRelationNames(applicant);
        applicantService.validApplicant(applicant, true);
        User loginUser = userService.getLoginUser(request);
        applicant.setCreateUserId(loginUser.getId());
        boolean result = applicantService.save(applicant);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(applicant.getId());
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteApplicant(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Applicant old = applicantService.getById(id);
        ThrowUtils.throwIf(old == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = applicantService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateApplicant(@RequestBody ApplicantUpdateRequest updateRequest) {
        if (updateRequest == null || updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Applicant old = applicantService.getById(updateRequest.getId());
        ThrowUtils.throwIf(old == null, ErrorCode.NOT_FOUND_ERROR);
        Applicant applicant = new Applicant();
        BeanUtils.copyProperties(updateRequest, applicant);
        applicantService.fillRelationNames(applicant);
        applicantService.validApplicant(applicant, false);
        boolean result = applicantService.updateById(applicant);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @GetMapping("/get/vo")
    public BaseResponse<ApplicantVO> getApplicantVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        Applicant applicant = applicantService.getById(id);
        ThrowUtils.throwIf(applicant == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(applicantService.getApplicantVO(applicant));
    }

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ApplicantVO>> listApplicantVOByPage(
            @RequestBody ApplicantQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<Applicant> page = applicantService.page(new Page<>(current, size),
                applicantService.getQueryWrapper(queryRequest));
        return ResultUtils.success(applicantService.getApplicantVOPage(page));
    }
}
