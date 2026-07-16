package com.springbootinit.controller;


import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.model.dto.recruitment.RecruitmentAddRequest;
import com.springbootinit.model.entity.Recruitment;
import com.springbootinit.model.entity.User;
import com.springbootinit.service.RecruitmentService;
import com.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/recruitment")
@Slf4j
public class RecruitmentController {

    @Resource
    private UserService userService;


    @Resource
    private RecruitmentService recruitmentService;


    @PostMapping("/add")
    public BaseResponse<Long> addRecruitment(@RequestBody RecruitmentAddRequest recruitmentAddRequest,
                                             HttpServletRequest request) {
        ThrowUtils.throwIf(recruitmentAddRequest == null, ErrorCode.PARAMS_ERROR);
        Recruitment recruitment = new Recruitment();
        BeanUtils.copyProperties(recruitmentAddRequest, recruitment);
        recruitmentService.validRecruitment(recruitment,true);
        User loginUser = userService.getLoginUser(request);
        recruitment.setCreateUserId(loginUser.getId());
        if (recruitment.getStatus() == null) {
            recruitment.setStatus(1);
        }
        boolean result = recruitmentService.save(recruitment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return  ResultUtils.success(recruitment.getId());
    }


}
