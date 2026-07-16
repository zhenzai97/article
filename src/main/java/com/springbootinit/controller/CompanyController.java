package com.springbootinit.controller;


import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/company")
@Slf4j
public class CompanyController {

    @Resource
    private UserService userService;

    @Resource
    private CompanyService companyService;

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


    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteCompany(@PathVariable("id") long id) {
        if (id <= 0) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Company company = companyService.getById(id);
        ThrowUtils.throwIf(company == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = companyService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return  ResultUtils.success(true);
    }


    @PostMapping("/update")
    public BaseResponse<Boolean> updateCompany(@RequestBody CompanyUpdateRequest companyUpdateRequest) {
        ThrowUtils.throwIf(companyUpdateRequest ==null, ErrorCode.PARAMS_ERROR);
        Company company = new Company();
        BeanUtils.copyProperties(companyUpdateRequest, company);
        companyService.validCompany(company,true);
        boolean result = companyService.updateById(company);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }


    @PostMapping("/list/page/vo")
    public BaseResponse<Page<CompanyVO>> listCompanyVOByPage(@RequestBody CompanyQueryRequest  companyQueryRequest) {
        long current = companyQueryRequest.getCurrent();
        long size = companyQueryRequest.getPageSize();
        Page<Company> companyPage = companyService.page(new Page<>(current, size),
                companyService.getQueryWrapper(companyQueryRequest));
        return ResultUtils.success(companyService.getCompanyVOPage(companyPage));
    }
}
