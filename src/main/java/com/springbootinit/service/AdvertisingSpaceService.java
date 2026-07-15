package com.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springbootinit.model.dto.advertisingSpace.AdvertisingSpaceQueryRequest;
import com.springbootinit.model.entity.AdvertisingSpace;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootinit.model.vo.AdvertisingSpaceVO;

public interface AdvertisingSpaceService extends IService<AdvertisingSpace> {

    /**
     * 校验数据
     */
    void validAdvertisingSpace(AdvertisingSpace advertisingSpace, boolean add);

    /**
     * 获取查询条件
     */
   QueryWrapper<AdvertisingSpace> getQueryWrapper(AdvertisingSpaceQueryRequest advertisingSpaceQueryRequest);


    /**
     * 分页获取运营分类封装
     */
    Page<AdvertisingSpaceVO>  getAdvertisingSpaceVOPage(Page<AdvertisingSpace> advertisingSpacePage);


    /**
     * 获取文获取运营分类
     */
    AdvertisingSpaceVO getAdvertisingSpaceVO(AdvertisingSpace advertisingSpace);

}
