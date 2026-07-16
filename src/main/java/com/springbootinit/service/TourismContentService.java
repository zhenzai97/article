package com.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springbootinit.model.dto.tourism.TourismContentQueryRequest;
import com.springbootinit.model.entity.TourismContent;
import com.springbootinit.model.vo.TourismContentVO;
import java.util.List;

/**
 * 文旅内容服务
 */
public interface TourismContentService extends IService<TourismContent> {

    void validTourismContent(TourismContent tourismContent, boolean add);

    QueryWrapper<TourismContent> getQueryWrapper(TourismContentQueryRequest request);

    TourismContentVO getTourismContentVO(TourismContent tourismContent);

    Page<TourismContentVO> getTourismContentVOPage(Page<TourismContent> page);

    /** album List ↔ JSON 字符串 */
    String toAlbumJson(List<String> album);

    List<String> parseAlbum(String albumJson);
}
