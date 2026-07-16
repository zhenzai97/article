package com.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.constant.CommonConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.mapper.AdvertisingMapper;
import com.springbootinit.model.dto.advertising.AdvertisingQueryRequest;
import com.springbootinit.model.entity.Advertising;
import com.springbootinit.model.entity.AdvertisingSpace;
import com.springbootinit.model.vo.AdvertisingVO;
import com.springbootinit.service.AdvertisingService;
import com.springbootinit.service.AdvertisingSpaceService;
import com.springbootinit.utils.SqlUtils;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 运营广告服务实现
 */
@Service
public class AdvertisingServiceImpl extends ServiceImpl<AdvertisingMapper, Advertising>
        implements AdvertisingService {

    @Resource
    private AdvertisingSpaceService advertisingSpaceService;

    @Override
    public void validAdvertising(Advertising advertising, boolean add) {
        ThrowUtils.throwIf(advertising == null, ErrorCode.PARAMS_ERROR);
        String name = advertising.getName();
        Long spaceId = advertising.getSpaceId();
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.PARAMS_ERROR, "广告标题不能为空");
            ThrowUtils.throwIf(spaceId == null || spaceId <= 0, ErrorCode.PARAMS_ERROR, "运营位不能为空");
        }
        if (StringUtils.isNotBlank(name) && name.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "广告标题过长");
        }
        if (spaceId != null && spaceId > 0) {
            AdvertisingSpace space = advertisingSpaceService.getById(spaceId);
            ThrowUtils.throwIf(space == null, ErrorCode.PARAMS_ERROR, "运营位不存在");
        }
        if (advertising.getStartTime() != null && advertising.getEndTime() != null
                && advertising.getStartTime().after(advertising.getEndTime())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "开始日期不能晚于结束日期");
        }
    }

    @Override
    public QueryWrapper<Advertising> getQueryWrapper(AdvertisingQueryRequest request) {
        QueryWrapper<Advertising> queryWrapper = new QueryWrapper<>();
        if (request == null) {
            return queryWrapper;
        }
        Long id = request.getId();
        Long spaceId = request.getSpaceId();
        String name = request.getName();
        Integer status = request.getStatus();
        String sortField = request.getSortField();
        String sortOrder = request.getSortOrder();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(spaceId != null, "spaceId", spaceId);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(status != null, "status", status);

        // 时间范围筛选
        SqlUtils.applyDateTimeRange(queryWrapper, "createTime", request.getCStartTime(), request.getCEndTime());
        SqlUtils.applyDateTimeRange(queryWrapper, "updateTime", request.getUpStartTime(), request.getUpEndTime());
        SqlUtils.applyDateRange(queryWrapper, "startTime", request.getSStartTime(), request.getSEndTime());
        SqlUtils.applyDateRange(queryWrapper, "endTime", request.getEStartTime(), request.getEEndTime());

        if (SqlUtils.validSortField(sortField)) {
            queryWrapper.orderBy(true, CommonConstant.SORT_ORDER_ASC.equals(sortOrder), sortField);
        } else {
            queryWrapper.orderByDesc("sort").orderByDesc("updateTime");
        }
        return queryWrapper;
    }

    @Override
    public AdvertisingVO getAdvertisingVO(Advertising advertising) {
        if (advertising == null) {
            return null;
        }
        AdvertisingVO vo = new AdvertisingVO();
        BeanUtils.copyProperties(advertising, vo);
        if (advertising.getSpaceId() != null) {
            AdvertisingSpace space = advertisingSpaceService.getById(advertising.getSpaceId());
            if (space != null) {
                vo.setSpaceName(space.getName());
                vo.setSpaceSign(space.getSign());
            }
        }
        return vo;
    }

    @Override
    public Page<AdvertisingVO> getAdvertisingVOPage(Page<Advertising> advertisingPage) {
        List<Advertising> list = advertisingPage.getRecords();
        Page<AdvertisingVO> voPage = new Page<>(advertisingPage.getCurrent(), advertisingPage.getSize(),
                advertisingPage.getTotal());
        if (CollUtil.isEmpty(list)) {
            return voPage;
        }
        List<AdvertisingVO> voList = list.stream().map(this::getAdvertisingVO).collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }
}
