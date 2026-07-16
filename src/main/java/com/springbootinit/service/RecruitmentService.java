package com.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springbootinit.model.entity.Recruitment;

public interface RecruitmentService extends IService<Recruitment> {


    void validRecruitment(Recruitment recruitment, boolean add);

}
