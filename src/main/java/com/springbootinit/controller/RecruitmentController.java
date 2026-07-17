package com.springbootinit.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.model.dto.recruitment.RecruitmentAddRequest;
import com.springbootinit.model.dto.recruitment.RecruitmentQueryRequest;
import com.springbootinit.model.dto.recruitment.RecruitmentUpdateRequest;
import com.springbootinit.model.entity.Company;
import com.springbootinit.model.entity.Recruitment;
import com.springbootinit.model.entity.User;
import com.springbootinit.model.vo.RecruitmentVO;
import com.springbootinit.service.CompanyService;
import com.springbootinit.service.RecruitmentService;
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
 * 招聘列表接口
 */
@RestController
@RequestMapping("/recruitment")
@Slf4j
public class RecruitmentController {

    @Resource
    private UserService userService;

    @Resource
    private RecruitmentService recruitmentService;

    @Resource
    private CompanyService companyService;

    @PostMapping("/add")
    public BaseResponse<Long> addRecruitment(@RequestBody RecruitmentAddRequest recruitmentAddRequest,
            HttpServletRequest request) {
        ThrowUtils.throwIf(recruitmentAddRequest == null, ErrorCode.PARAMS_ERROR);
        Recruitment recruitment = new Recruitment();
        BeanUtils.copyProperties(recruitmentAddRequest, recruitment);
        fillCompanyName(recruitment);
        recruitmentService.validRecruitment(recruitment, true);
        User loginUser = userService.getLoginUser(request);
        recruitment.setCreateUserId(loginUser.getId());
        if (recruitment.getStatus() == null) {
            recruitment.setStatus(1);
        }
        if (recruitment.getSort() == null) {
            recruitment.setSort(0);
        }
        boolean result = recruitmentService.save(recruitment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(recruitment.getId());
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteRecruitment(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Recruitment recruitment = recruitmentService.getById(id);
        ThrowUtils.throwIf(recruitment == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = recruitmentService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateRecruitment(@RequestBody RecruitmentUpdateRequest recruitmentUpdateRequest) {
        if (recruitmentUpdateRequest == null || recruitmentUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Recruitment old = recruitmentService.getById(recruitmentUpdateRequest.getId());
        ThrowUtils.throwIf(old == null, ErrorCode.NOT_FOUND_ERROR);
        Recruitment recruitment = new Recruitment();
        BeanUtils.copyProperties(recruitmentUpdateRequest, recruitment);
        fillCompanyName(recruitment);
        recruitmentService.validRecruitment(recruitment, false);
        boolean result = recruitmentService.updateById(recruitment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @GetMapping("/get/vo")
    public BaseResponse<RecruitmentVO> getRecruitmentVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        Recruitment recruitment = recruitmentService.getById(id);
        ThrowUtils.throwIf(recruitment == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(recruitmentService.getRecruitmentVO(recruitment));
    }

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<RecruitmentVO>> listRecruitmentVOByPage(
            @RequestBody RecruitmentQueryRequest recruitmentQueryRequest) {
        long current = recruitmentQueryRequest.getCurrent();
        long size = recruitmentQueryRequest.getPageSize();
        Page<Recruitment> page = recruitmentService.page(new Page<>(current, size),
                recruitmentService.getQueryWrapper(recruitmentQueryRequest));
        return ResultUtils.success(recruitmentService.getRecruitmentVOPage(page));
    }

    private void fillCompanyName(Recruitment recruitment) {
        Long companyId = recruitment.getCompanyId();
        if (companyId == null || companyId <= 0) {
            recruitment.setCompanyName(null);
            return;
        }
        Company company = companyService.getById(companyId);
        ThrowUtils.throwIf(company == null, ErrorCode.PARAMS_ERROR, "公司不存在");
        recruitment.setCompanyName(company.getName());
    }
}
