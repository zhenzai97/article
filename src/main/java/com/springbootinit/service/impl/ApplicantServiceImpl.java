package com.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.constant.CommonConstant;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.mapper.ApplicantMapper;
import com.springbootinit.model.dto.applicant.ApplicantQueryRequest;
import com.springbootinit.model.entity.Applicant;
import com.springbootinit.model.entity.Company;
import com.springbootinit.model.entity.Recruitment;
import com.springbootinit.model.vo.ApplicantVO;
import com.springbootinit.service.ApplicantService;
import com.springbootinit.service.CompanyService;
import com.springbootinit.service.RecruitmentService;
import com.springbootinit.utils.SqlUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ApplicantServiceImpl extends ServiceImpl<ApplicantMapper, Applicant> implements ApplicantService {

    @Resource
    private CompanyService companyService;

    @Lazy
    @Resource
    private RecruitmentService recruitmentService;

    @Override
    public void validApplicant(Applicant applicant, boolean add) {
        ThrowUtils.throwIf(applicant == null, ErrorCode.PARAMS_ERROR);
        if (!add) {
            return;
        }
        ThrowUtils.throwIf(StringUtils.isBlank(applicant.getName()), ErrorCode.PARAMS_ERROR, "应聘人姓名不能为空");
        ThrowUtils.throwIf(StringUtils.isBlank(applicant.getMobile()), ErrorCode.PARAMS_ERROR, "联系电话不能为空");
        ThrowUtils.throwIf(applicant.getRecruitmentId() == null || applicant.getRecruitmentId() <= 0,
                ErrorCode.PARAMS_ERROR, "请选择应聘岗位");
    }

    @Override
    public void fillRelationNames(Applicant applicant) {
        if (applicant == null) {
            return;
        }
        Long companyId = applicant.getCompanyId();
        if (companyId != null && companyId > 0) {
            Company company = companyService.getById(companyId);
            ThrowUtils.throwIf(company == null, ErrorCode.PARAMS_ERROR, "公司不存在");
            applicant.setCompanyName(company.getName());
        } else {
            applicant.setCompanyName(null);
        }

        Long recruitmentId = applicant.getRecruitmentId();
        if (recruitmentId != null && recruitmentId > 0) {
            Recruitment recruitment = recruitmentService.getById(recruitmentId);
            ThrowUtils.throwIf(recruitment == null, ErrorCode.PARAMS_ERROR, "招聘岗位不存在");
            applicant.setRecruitmentName(recruitment.getName());
            // 未传公司时，用岗位所属公司补齐
            if (applicant.getCompanyId() == null && recruitment.getCompanyId() != null) {
                applicant.setCompanyId(recruitment.getCompanyId());
                applicant.setCompanyName(recruitment.getCompanyName());
            }
        } else {
            applicant.setRecruitmentName(null);
        }
    }

    @Override
    public QueryWrapper<Applicant> getQueryWrapper(ApplicantQueryRequest queryRequest) {
        QueryWrapper<Applicant> queryWrapper = new QueryWrapper<>();
        if (queryRequest == null) {
            return queryWrapper;
        }
        Long id = queryRequest.getId();
        String name = queryRequest.getName();
        String mobile = queryRequest.getMobile();
        Long companyId = queryRequest.getCompanyId();
        Long recruitmentId = queryRequest.getRecruitmentId();
        String salaryRange = queryRequest.getSalaryRange();
        String sortField = queryRequest.getSortField();
        String sortOrder = queryRequest.getSortOrder();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(mobile), "mobile", mobile);
        queryWrapper.eq(companyId != null, "companyId", companyId);
        queryWrapper.eq(recruitmentId != null, "recruitmentId", recruitmentId);
        queryWrapper.eq(StringUtils.isNotBlank(salaryRange), "salaryRange", salaryRange);

        SqlUtils.applyDateTimeRange(queryWrapper, "createTime",
                queryRequest.getCStartTime(), queryRequest.getCEndTime());
        SqlUtils.applyDateTimeRange(queryWrapper, "updateTime",
                queryRequest.getUpStartTime(), queryRequest.getUpEndTime());

        if (SqlUtils.validSortField(sortField)) {
            queryWrapper.orderBy(true, CommonConstant.SORT_ORDER_ASC.equals(sortOrder), sortField);
        } else {
            queryWrapper.orderByDesc("createTime");
        }
        return queryWrapper;
    }

    @Override
    public ApplicantVO getApplicantVO(Applicant applicant) {
        if (applicant == null) {
            return null;
        }
        ApplicantVO vo = new ApplicantVO();
        BeanUtils.copyProperties(applicant, vo);
        return vo;
    }

    @Override
    public Page<ApplicantVO> getApplicantVOPage(Page<Applicant> applicantPage) {
        List<Applicant> list = applicantPage.getRecords();
        Page<ApplicantVO> voPage = new Page<>(applicantPage.getCurrent(), applicantPage.getSize(),
                applicantPage.getTotal());
        if (CollUtil.isEmpty(list)) {
            return voPage;
        }
        voPage.setRecords(list.stream().map(this::getApplicantVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public Map<Long, Long> countByRecruitmentIds(Collection<Long> recruitmentIds) {
        if (CollUtil.isEmpty(recruitmentIds)) {
            return Collections.emptyMap();
        }
        List<Applicant> list = this.list(new QueryWrapper<Applicant>()
                .in("recruitmentId", recruitmentIds)
                .select("recruitmentId"));
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        Map<Long, Long> map = new HashMap<>();
        for (Applicant item : list) {
            if (item.getRecruitmentId() == null) {
                continue;
            }
            map.merge(item.getRecruitmentId(), 1L, Long::sum);
        }
        return map;
    }
}
