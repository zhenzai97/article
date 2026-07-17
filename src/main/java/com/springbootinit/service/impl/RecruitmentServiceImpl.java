package com.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.constant.CommonConstant;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.mapper.RecruitmentMapper;
import com.springbootinit.model.dto.recruitment.RecruitmentQueryRequest;
import com.springbootinit.model.entity.Company;
import com.springbootinit.model.entity.Recruitment;
import com.springbootinit.model.vo.RecruitmentVO;
import com.springbootinit.service.ApplicantService;
import com.springbootinit.service.CompanyService;
import com.springbootinit.service.RecruitmentService;
import javax.annotation.Resource;

import com.springbootinit.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecruitmentServiceImpl extends ServiceImpl<RecruitmentMapper,Recruitment> implements RecruitmentService {

    @Resource
    private CompanyService companyService;

    @Lazy
    @Resource
    private ApplicantService applicantService;

    @Override
    public void validRecruitment(Recruitment recruitment, boolean add){
        ThrowUtils.throwIf(recruitment == null, ErrorCode.PARAMS_ERROR);
        Long  companyId = recruitment.getCompanyId();

        if(add){
            String name = recruitment.getName();
            String salaryRange  = recruitment.getSalaryRange();
            String workingHours = recruitment.getWorkingHours();
            String qualification= recruitment.getQualification();
            String salaryContent  = recruitment.getSalaryContent();
            String positionContent  = recruitment.getPositionContent();

            ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.PARAMS_ERROR, "招聘标题不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(salaryRange), ErrorCode.PARAMS_ERROR, "薪资范围不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(workingHours), ErrorCode.PARAMS_ERROR, "工作时长不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(qualification), ErrorCode.PARAMS_ERROR, "学历不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(salaryContent), ErrorCode.PARAMS_ERROR, "薪资详情不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(positionContent), ErrorCode.PARAMS_ERROR, "岗位详情不能为空");
        }
        if (companyId != null && companyId > 0) {
            Company company = companyService.getById(companyId);
            ThrowUtils.throwIf(company == null, ErrorCode.PARAMS_ERROR, "公司不存在");
        }
    }

    @Override
    public QueryWrapper<Recruitment> getQueryWrapper(RecruitmentQueryRequest recruitmentQueryRequest) {
        QueryWrapper<Recruitment> queryWrapper = new QueryWrapper<>();
        if (recruitmentQueryRequest == null) {
            return queryWrapper;
        }
        Long id = recruitmentQueryRequest.getId();
        String name = recruitmentQueryRequest.getName();
        String salaryRange = recruitmentQueryRequest.getSalaryRange();
        String workingHours = recruitmentQueryRequest.getWorkingHours();
        String qualification = recruitmentQueryRequest.getQualification();
        Long companyId = recruitmentQueryRequest.getCompanyId();
        Integer status=  recruitmentQueryRequest.getStatus();
        String sortField = recruitmentQueryRequest.getSortField();
        String sortOrder = recruitmentQueryRequest.getSortOrder();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(StringUtils.isNotBlank(salaryRange), "salaryRange", salaryRange);
        queryWrapper.eq(StringUtils.isNotBlank(workingHours), "workingHours", workingHours);
        queryWrapper.eq(StringUtils.isNotBlank(qualification), "qualification", qualification);
        queryWrapper.eq(companyId != null, "companyId", companyId);
        queryWrapper.eq(status != null, "status", status);

        SqlUtils.applyDateTimeRange(queryWrapper, "createTime",
                recruitmentQueryRequest.getCStartTime(), recruitmentQueryRequest.getCEndTime());
        SqlUtils.applyDateTimeRange(queryWrapper, "updateTime",
                recruitmentQueryRequest.getUpStartTime(), recruitmentQueryRequest.getUpEndTime());

        if (SqlUtils.validSortField(sortField)) {
            queryWrapper.orderBy(true, CommonConstant.SORT_ORDER_ASC.equals(sortOrder), sortField);
        } else {
            queryWrapper.orderByDesc("sort").orderByDesc("updateTime");
        }
        return queryWrapper;
    }

    @Override
    public RecruitmentVO getRecruitmentVO(Recruitment recruitment){
        if (recruitment == null) {
            return null;
        }
        RecruitmentVO recruitmentVO = new RecruitmentVO();
        BeanUtils.copyProperties(recruitment, recruitmentVO);
        if (recruitment.getId() != null) {
            Map<Long, Long> countMap = applicantService.countByRecruitmentIds(
                    java.util.Collections.singletonList(recruitment.getId()));
            recruitmentVO.setApplicantCount(countMap.getOrDefault(recruitment.getId(), 0L));
        } else {
            recruitmentVO.setApplicantCount(0L);
        }
        return recruitmentVO;
    }

    @Override
    public Page<RecruitmentVO> getRecruitmentVOPage(Page<Recruitment> recruitmentPage){
       List<Recruitment> recruitmentList =  recruitmentPage.getRecords();

       Page<RecruitmentVO> recruitmentVOPage = new Page<>(recruitmentPage.getCurrent(), recruitmentPage.getSize(), recruitmentPage.getTotal());
       if(CollUtil.isEmpty(recruitmentList)){
           return recruitmentVOPage;
       }
        List<Long> ids = recruitmentList.stream().map(Recruitment::getId).collect(Collectors.toList());
        Map<Long, Long> countMap = applicantService.countByRecruitmentIds(ids);
        List<RecruitmentVO> recruitmentVOList = recruitmentList.stream().map(item -> {
            RecruitmentVO vo = new RecruitmentVO();
            BeanUtils.copyProperties(item, vo);
            vo.setApplicantCount(countMap.getOrDefault(item.getId(), 0L));
            return vo;
        }).collect(Collectors.toList());
        recruitmentVOPage.setRecords(recruitmentVOList);
        return  recruitmentVOPage;
    }


}
