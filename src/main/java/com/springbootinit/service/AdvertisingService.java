package com.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springbootinit.model.dto.advertising.AdvertisingQueryRequest;
import com.springbootinit.model.entity.Advertising;
import com.springbootinit.model.vo.AdvertisingVO;

/**
 * 运营广告服务
 */
public interface AdvertisingService extends IService<Advertising> {

    void validAdvertising(Advertising advertising, boolean add);

    QueryWrapper<Advertising> getQueryWrapper(AdvertisingQueryRequest advertisingQueryRequest);

    AdvertisingVO getAdvertisingVO(Advertising advertising);

    Page<AdvertisingVO> getAdvertisingVOPage(Page<Advertising> advertisingPage);
}
