package com.springbootinit.controller;

import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.model.vo.HomeIndexVO;
import com.springbootinit.service.HomeSectionService;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * C 端首页公开接口（无需登录）
 */
@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    @Resource
    private HomeSectionService homeSectionService;

    /**
     * 首页聚合数据
     */
    @GetMapping("/index")
    public BaseResponse<HomeIndexVO> index() {
        return ResultUtils.success(homeSectionService.buildHomeIndex());
    }
}
