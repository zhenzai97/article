package com.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.constant.CommonConstant;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.exception.ThrowUtils;
import com.springbootinit.mapper.TourismContentMapper;
import com.springbootinit.model.dto.tourism.TourismContentQueryRequest;
import com.springbootinit.model.entity.TourismContent;
import com.springbootinit.model.vo.TourismContentVO;
import com.springbootinit.service.TourismContentService;
import com.springbootinit.utils.SqlUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 文旅内容服务实现
 */
@Service
public class TourismContentServiceImpl extends ServiceImpl<TourismContentMapper, TourismContent>
        implements TourismContentService {

    private static final Set<String> ALLOWED_TYPES = new HashSet<>(Arrays.asList(
            "product", "brand", "market", "jewelry", "food", "scenic"));

    @Override
    public void validTourismContent(TourismContent tourismContent, boolean add) {
        ThrowUtils.throwIf(tourismContent == null, ErrorCode.PARAMS_ERROR);
        String name = tourismContent.getName();
        String type = tourismContent.getType();
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.PARAMS_ERROR, "名称不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR, "类型不能为空");
        }
        if (StringUtils.isNotBlank(type) && !ALLOWED_TYPES.contains(type)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不支持的内容类型");
        }
        if (StringUtils.isNotBlank(name) && name.length() > 256) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    @Override
    public QueryWrapper<TourismContent> getQueryWrapper(TourismContentQueryRequest request) {
        QueryWrapper<TourismContent> queryWrapper = new QueryWrapper<>();
        if (request == null) {
            return queryWrapper;
        }
        Long id = request.getId();
        String type = request.getType();
        String name = request.getName();
        Integer status = request.getStatus();
        Integer isRecommend = request.getIsRecommend();
        String sortField = request.getSortField();
        String sortOrder = request.getSortOrder();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(type), "type", type);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.eq(isRecommend != null, "isRecommend", isRecommend);

        SqlUtils.applyDateTimeRange(queryWrapper, "createTime", request.getCStartTime(), request.getCEndTime());
        SqlUtils.applyDateTimeRange(queryWrapper, "updateTime", request.getUpStartTime(), request.getUpEndTime());

        if (SqlUtils.validSortField(sortField)) {
            queryWrapper.orderBy(true, CommonConstant.SORT_ORDER_ASC.equals(sortOrder), sortField);
        } else {
            queryWrapper.orderByDesc("sort").orderByDesc("updateTime");
        }
        return queryWrapper;
    }

    @Override
    public TourismContentVO getTourismContentVO(TourismContent tourismContent) {
        if (tourismContent == null) {
            return null;
        }
        TourismContentVO vo = new TourismContentVO();
        BeanUtils.copyProperties(tourismContent, vo);
        vo.setAlbum(parseAlbum(tourismContent.getAlbum()));
        return vo;
    }

    @Override
    public Page<TourismContentVO> getTourismContentVOPage(Page<TourismContent> page) {
        List<TourismContent> list = page.getRecords();
        Page<TourismContentVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (CollUtil.isEmpty(list)) {
            return voPage;
        }
        List<TourismContentVO> voList = list.stream().map(this::getTourismContentVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public String toAlbumJson(List<String> album) {
        if (CollUtil.isEmpty(album)) {
            return null;
        }
        List<String> cleaned = album.stream()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        if (cleaned.isEmpty()) {
            return null;
        }
        return JSONUtil.toJsonStr(cleaned);
    }

    @Override
    public List<String> parseAlbum(String albumJson) {
        if (StringUtils.isBlank(albumJson)) {
            return new ArrayList<>();
        }
        try {
            return JSONUtil.toList(albumJson, String.class);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
