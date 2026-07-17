package com.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.constant.CommonConstant;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.mapper.CompanyMapper;
import com.springbootinit.model.dto.company.CompanyQueryRequest;
import com.springbootinit.model.entity.Company;
import com.springbootinit.model.vo.CompanyVO;
import com.springbootinit.service.CompanyService;
import com.springbootinit.utils.SqlUtils;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

    @Override
    public void validCompany(Company company, boolean add) {
        ThrowUtils.throwIf(company == null, ErrorCode.PARAMS_ERROR);
        if (!add) {
            return;
        }
        Integer vipType = company.getVipType();
        ThrowUtils.throwIf(vipType == null, ErrorCode.PARAMS_ERROR, "会员类型不能为空");
        String mobile = company.getMobile();
        String address = company.getAddress();
        if (vipType == 1) {
            ThrowUtils.throwIf(StringUtils.isBlank(company.getName()), ErrorCode.PARAMS_ERROR, "单位名称不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(company.getNickname()), ErrorCode.PARAMS_ERROR, "单位简介不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(company.getLicense()), ErrorCode.PARAMS_ERROR, "营业执照不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(company.getBusiness()), ErrorCode.PARAMS_ERROR, "公司业务不能为空");
        } else if (vipType == 2) {
            ThrowUtils.throwIf(StringUtils.isBlank(company.getName()), ErrorCode.PARAMS_ERROR, "姓名不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(company.getNickname()), ErrorCode.PARAMS_ERROR, "平台用户名称不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(company.getIntro()), ErrorCode.PARAMS_ERROR, "个人简介不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(company.getBusiness()), ErrorCode.PARAMS_ERROR, "所在行业不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(company.getTag()), ErrorCode.PARAMS_ERROR, "个人标签不能为空");
        } else {
            ThrowUtils.throwIf(true, ErrorCode.PARAMS_ERROR, "会员类型不正确");
        }
        ThrowUtils.throwIf(StringUtils.isBlank(mobile), ErrorCode.PARAMS_ERROR, "联系方式不能为空");
        ThrowUtils.throwIf(StringUtils.isBlank(address), ErrorCode.PARAMS_ERROR, "详细地址不能为空");
    }

    @Override
    public void validCompanyAdmin(Company company, boolean add) {
        ThrowUtils.throwIf(company == null, ErrorCode.PARAMS_ERROR);
        if (!add) {
            return;
        }
        Integer vipType = company.getVipType();
        ThrowUtils.throwIf(vipType == null, ErrorCode.PARAMS_ERROR, "会员类型不能为空");
        if (vipType == 1) {
            // 单位会员：公司业务、联系电话不必填
            ThrowUtils.throwIf(StringUtils.isBlank(company.getName()), ErrorCode.PARAMS_ERROR, "单位名称不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(company.getNickname()), ErrorCode.PARAMS_ERROR, "单位简称不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(company.getLicense()), ErrorCode.PARAMS_ERROR, "营业执照不能为空");
        } else if (vipType == 2) {
            // 个人会员：所在行业、个人简介、个人标签、联系电话不必填
            ThrowUtils.throwIf(StringUtils.isBlank(company.getName()), ErrorCode.PARAMS_ERROR, "姓名不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(company.getNickname()), ErrorCode.PARAMS_ERROR, "平台用户名称不能为空");
        } else {
            ThrowUtils.throwIf(true, ErrorCode.PARAMS_ERROR, "会员类型不正确");
        }
        ThrowUtils.throwIf(StringUtils.isBlank(company.getAddress()), ErrorCode.PARAMS_ERROR, "详细地址不能为空");
    }

    @Override
    public QueryWrapper<Company> getQueryWrapper(CompanyQueryRequest companyQueryRequest) {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();

        if (companyQueryRequest == null) {
            return queryWrapper;
        }
        Long id = companyQueryRequest.getId();
        String name = companyQueryRequest.getName();
        String mobile = companyQueryRequest.getMobile();
        String nickname = companyQueryRequest.getNickname();
        Integer vipType = companyQueryRequest.getVipType();
        Integer vip = companyQueryRequest.getVip();
        Integer examineStatus = companyQueryRequest.getExamineStatus();
        Integer status = companyQueryRequest.getStatus();
        String tag = companyQueryRequest.getTag();
        String sortField = companyQueryRequest.getSortField();
        String sortOrder = companyQueryRequest.getSortOrder();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(nickname), "nickname", nickname);
        queryWrapper.like(StringUtils.isNotBlank(mobile), "mobile", mobile);
        queryWrapper.eq(vip != null, "vip", vip);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.eq(examineStatus != null, "examineStatus", examineStatus);
        queryWrapper.eq(vipType != null, "vipType", vipType);
        queryWrapper.like(StringUtils.isNotBlank(tag), "tag", tag);
        SqlUtils.applyDateTimeRange(queryWrapper, "createTime",
                companyQueryRequest.getCStartTime(), companyQueryRequest.getCEndTime());
        SqlUtils.applyDateTimeRange(queryWrapper, "updateTime",
                companyQueryRequest.getUpStartTime(), companyQueryRequest.getUpEndTime());

        if (SqlUtils.validSortField(sortField)) {
            queryWrapper.orderBy(true, CommonConstant.SORT_ORDER_ASC.equals(sortOrder), sortField);
        } else {
            queryWrapper.orderByDesc("sort").orderByDesc("updateTime");
        }
        return queryWrapper;
    }

    @Override
    public CompanyVO getCompanyVO(Company company) {
        if (company == null) {
            return null;
        }
        CompanyVO companyVO = new CompanyVO();
        BeanUtils.copyProperties(company, companyVO);
        return companyVO;
    }

    @Override
    public Page<CompanyVO> getCompanyVOPage(Page<Company> companyPage) {
        List<Company> companyList = companyPage.getRecords();
        Page<CompanyVO> companyVOPage = new Page<>(companyPage.getCurrent(), companyPage.getSize(), companyPage.getTotal());
        if (CollUtil.isEmpty(companyList)) {
            return companyVOPage;
        }
        List<CompanyVO> companyVOList = companyList.stream().map(this::getCompanyVO).collect(Collectors.toList());
        companyVOPage.setRecords(companyVOList);
        return companyVOPage;
    }
}
