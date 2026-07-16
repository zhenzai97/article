package com.springbootinit.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springbootinit.model.dto.company.CompanyQueryRequest;
import com.springbootinit.model.entity.Company;
import com.springbootinit.model.vo.CompanyVO;

public interface CompanyService extends IService<Company> {

    void validCompany(Company company, boolean add);

    QueryWrapper<Company> getQueryWrapper(CompanyQueryRequest companyQueryRequest);

    Page<CompanyVO> getCompanyVOPage(Page<Company> companyPage);

    CompanyVO getCompanyVO(Company company);
}
