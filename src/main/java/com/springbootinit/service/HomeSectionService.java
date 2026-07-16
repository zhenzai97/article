package com.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springbootinit.model.entity.HomeSection;
import com.springbootinit.model.vo.HomeIndexVO;
import com.springbootinit.model.vo.HomeSectionVO;
import java.util.List;

/**
 * 首页区块服务
 */
public interface HomeSectionService extends IService<HomeSection> {

    List<HomeSectionVO> listAllVO();

    HomeSectionVO getVO(HomeSection section);

    /** C 端首页聚合 */
    HomeIndexVO buildHomeIndex();
}
