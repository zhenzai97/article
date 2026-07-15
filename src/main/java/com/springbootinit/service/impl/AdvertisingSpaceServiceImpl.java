package com.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.mapper.AdvertisingSpaceMapper;
import com.springbootinit.model.dto.advertisingSpace.AdvertisingSpaceQueryRequest;
import com.springbootinit.model.entity.AdvertisingSpace;
import com.springbootinit.model.vo.AdvertisingSpaceVO;
import com.springbootinit.service.AdvertisingSpaceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdvertisingSpaceServiceImpl extends ServiceImpl<AdvertisingSpaceMapper, AdvertisingSpace> implements AdvertisingSpaceService {


    @Override
    public void validAdvertisingSpace(AdvertisingSpace advertisingSpace, boolean add) {
        ThrowUtils.throwIf(advertisingSpace == null, ErrorCode.PARAMS_ERROR);
        String sign = advertisingSpace.getSign();
        String name = advertisingSpace.getName();
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(sign, name), ErrorCode.PARAMS_ERROR, "分类标识和名称不能为空");
        }
        if (StringUtils.isNotBlank(sign) && sign.length() > 64) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类标识过长");
        }
        if (StringUtils.isNotBlank(name) && name.length() > 128) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类名称过长");
        }
    }

    @Override
    public QueryWrapper<AdvertisingSpace> getQueryWrapper(AdvertisingSpaceQueryRequest advertisingSpaceQueryRequest) {
        QueryWrapper<AdvertisingSpace> queryWrapper = new QueryWrapper<>();
        if (advertisingSpaceQueryRequest == null){
            return  queryWrapper;
        }
        Long id = advertisingSpaceQueryRequest.getId();
        String name = advertisingSpaceQueryRequest.getName();
        Integer status = advertisingSpaceQueryRequest.getStatus();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(name != null, "name", name);
        queryWrapper.eq(status != null, "status", status);
        return queryWrapper;
    }

    @Override
    public AdvertisingSpaceVO getAdvertisingSpaceVO(AdvertisingSpace advertisingSpace) {
        if (advertisingSpace == null) {
            return null;
        }
        AdvertisingSpaceVO advertisingSpaceVO = new AdvertisingSpaceVO();
        BeanUtils.copyProperties(advertisingSpace, advertisingSpaceVO);
        return advertisingSpaceVO;
    }


    @Override
    public Page<AdvertisingSpaceVO> getAdvertisingSpaceVOPage(Page<AdvertisingSpace> advertisingSpacePage){

      List<AdvertisingSpace> advertisingSpaceList=  advertisingSpacePage.getRecords();
      Page<AdvertisingSpaceVO> advertisingSpaceVOPage = new Page<>(advertisingSpacePage.getCurrent(), advertisingSpacePage.getSize(), advertisingSpacePage.getTotal());
      if(CollUtil.isEmpty(advertisingSpaceList)){
          return advertisingSpaceVOPage;
      }
      List<AdvertisingSpaceVO> advertisingSpaceVOList = advertisingSpaceList.stream().map(this::getAdvertisingSpaceVO)
              .collect(Collectors.toList());
        advertisingSpaceVOPage.setRecords(advertisingSpaceVOList);
        return advertisingSpaceVOPage;
    }
}
