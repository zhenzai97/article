package com.springbootinit.controller;

import com.springbootinit.annotation.AuthCheck;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.model.vo.DashboardOverviewVO;
import com.springbootinit.service.DashboardService;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工作台概览
 */
@RestController
@RequestMapping("/dashboard")
@Slf4j
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    /**
     * 聚合概览（统计 / 近期内容 / 趋势 / 栏目占比）
     */
    @GetMapping("/overview")
    @AuthCheck
    public BaseResponse<DashboardOverviewVO> overview() {
        return ResultUtils.success(dashboardService.getOverview());
    }
}
