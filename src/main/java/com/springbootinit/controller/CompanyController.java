package com.springbootinit.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.annotation.AuthCheck;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.constant.UserConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.model.dto.company.CompanyAddRequest;
import com.springbootinit.model.dto.company.CompanyQueryRequest;
import com.springbootinit.model.dto.company.CompanyUpdateRequest;
import com.springbootinit.model.entity.Company;
import com.springbootinit.model.entity.User;
import com.springbootinit.model.vo.CompanyVO;
import com.springbootinit.service.CompanyService;
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
 * 会员单位 / 企业接口
 */
@RestController
@RequestMapping("/company")
@Slf4j
public class CompanyController {

    @Resource
    private UserService userService;

    @Resource
    private CompanyService companyService;

    /**
     * 小程序入会申请（小程序在用，勿改）
     */
    @PostMapping("/app/add")
    public BaseResponse<Long> addCompany(@RequestBody CompanyAddRequest companyAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(companyAddRequest ==null, ErrorCode.PARAMS_ERROR);
        Company company = new Company();
        BeanUtils.copyProperties(companyAddRequest, company);
        companyService.validCompany(company,true);
        User loginUser = userService.getLoginUser(request);
        company.setCreateUserId(loginUser.getId());
        boolean result =   companyService.save(company);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return  ResultUtils.success(company.getId());
    }

    /**
     * 管理端新增会员单位
     */
    @PostMapping("/add")
    public BaseResponse<Long> addCompanyByAdmin(@RequestBody CompanyAddRequest companyAddRequest,
            HttpServletRequest request) {
        ThrowUtils.throwIf(companyAddRequest == null, ErrorCode.PARAMS_ERROR);
        Company company = new Company();
        BeanUtils.copyProperties(companyAddRequest, company);
        companyService.validCompanyAdmin(company, true);
        User loginUser = userService.getLoginUser(request);
        company.setCreateUserId(loginUser.getId());
        if (company.getStatus() == null) {
            company.setStatus(1);
        }
        if (company.getExamineStatus() == null) {
            company.setExamineStatus(1);
        }
        if (company.getSort() == null) {
            company.setSort(0);
        }
        boolean result = companyService.save(company);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(company.getId());
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteCompany(@PathVariable("id") long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Company company = companyService.getById(id);
        ThrowUtils.throwIf(company == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = companyService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateCompany(@RequestBody CompanyUpdateRequest companyUpdateRequest) {
        if (companyUpdateRequest == null || companyUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Company oldCompany = companyService.getById(companyUpdateRequest.getId());
        ThrowUtils.throwIf(oldCompany == null, ErrorCode.NOT_FOUND_ERROR);
        Company company = new Company();
        BeanUtils.copyProperties(companyUpdateRequest, company);
        companyService.validCompany(company, false);
        boolean result = companyService.updateById(company);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @GetMapping("/get/vo")
    public BaseResponse<CompanyVO> getCompanyVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        Company company = companyService.getById(id);
        ThrowUtils.throwIf(company == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(companyService.getCompanyVO(company));
    }

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<CompanyVO>> listCompanyVOByPage(@RequestBody CompanyQueryRequest companyQueryRequest) {
        long current = companyQueryRequest.getCurrent();
        long size = companyQueryRequest.getPageSize();
        Page<Company> companyPage = companyService.page(new Page<>(current, size),
                companyService.getQueryWrapper(companyQueryRequest));
        return ResultUtils.success(companyService.getCompanyVOPage(companyPage));
    }
}
