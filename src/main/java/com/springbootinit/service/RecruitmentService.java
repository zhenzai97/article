package com.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springbootinit.model.dto.recruitment.RecruitmentQueryRequest;
import com.springbootinit.model.entity.Recruitment;
import com.springbootinit.model.vo.RecruitmentVO;

public interface RecruitmentService extends IService<Recruitment> {

    void validRecruitment(Recruitment recruitment, boolean add);

    QueryWrapper<Recruitment> getQueryWrapper(RecruitmentQueryRequest recruitmentQueryRequest);

    RecruitmentVO getRecruitmentVO(Recruitment recruitment);

    Page<RecruitmentVO> getRecruitmentVOPage(Page<Recruitment> recruitmentPage);

}
