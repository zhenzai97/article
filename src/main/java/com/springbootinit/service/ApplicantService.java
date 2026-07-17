package com.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springbootinit.model.dto.applicant.ApplicantQueryRequest;
import com.springbootinit.model.entity.Applicant;
import com.springbootinit.model.vo.ApplicantVO;
import java.util.Collection;
import java.util.Map;

public interface ApplicantService extends IService<Applicant> {

    void validApplicant(Applicant applicant, boolean add);

    /** 填充公司名、岗位名冗余字段 */
    void fillRelationNames(Applicant applicant);

    QueryWrapper<Applicant> getQueryWrapper(ApplicantQueryRequest queryRequest);

    ApplicantVO getApplicantVO(Applicant applicant);

    Page<ApplicantVO> getApplicantVOPage(Page<Applicant> applicantPage);

    /** 按招聘岗位统计应聘人数 */
    Map<Long, Long> countByRecruitmentIds(Collection<Long> recruitmentIds);
}
