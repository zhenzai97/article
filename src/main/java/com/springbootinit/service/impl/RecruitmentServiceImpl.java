package com.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.mapper.RecruitmentMapper;
import com.springbootinit.model.entity.AdvertisingSpace;
import com.springbootinit.model.entity.Recruitment;
import com.springbootinit.service.RecruitmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class RecruitmentServiceImpl extends ServiceImpl<RecruitmentMapper,Recruitment> implements RecruitmentService {


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
            ThrowUtils.throwIf(StringUtils.isBlank(salaryRange), ErrorCode.PARAMS_ERROR, "招聘标题不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(workingHours), ErrorCode.PARAMS_ERROR, "工作时长不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(qualification), ErrorCode.PARAMS_ERROR, "学历不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(salaryContent), ErrorCode.PARAMS_ERROR, "薪资详情不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(positionContent), ErrorCode.PARAMS_ERROR, "岗位详情不能为空");
        }
        if (companyId != null && companyId > 0) {
//            AdvertisingSpace space = advertisingSpaceService.getById(spaceId);
            ThrowUtils.throwIf(false, ErrorCode.PARAMS_ERROR, "公司不存在");
        }
    }

}
