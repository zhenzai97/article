package ${packageName}.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${packageName}.common.ErrorCode;
import ${packageName}.constant.CommonConstant;
import ${packageName}.exception.BusinessException;
import ${packageName}.exception.ThrowUtils;
import ${packageName}.mapper.${upperDataKey}Mapper;
import ${packageName}.model.dto.${dataKey}.${upperDataKey}QueryRequest;
import ${packageName}.model.entity.ArticleCat;
import ${packageName}.model.entity.${upperDataKey};
import ${packageName}.model.vo.${upperDataKey}VO;
import ${packageName}.service.ArticleCatService;
import ${packageName}.service.${upperDataKey}Service;
import ${packageName}.utils.SqlUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * ${dataName}服务实现
 *
 */
@Service
public class ${upperDataKey}ServiceImpl extends ServiceImpl<${upperDataKey}Mapper, ${upperDataKey}> implements ${upperDataKey}Service {

    @Resource
    private ArticleCatService articleCatService;

    @Override
    public void valid${upperDataKey}(${upperDataKey} ${dataKey}, boolean add) {
        ThrowUtils.throwIf(${dataKey} == null, ErrorCode.PARAMS_ERROR);
        String title = ${dataKey}.getTitle();
        Long categoryId = ${dataKey}.getCategoryId();
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR, "标题不能为空");
            ThrowUtils.throwIf(categoryId == null || categoryId <= 0, ErrorCode.PARAMS_ERROR, "分类不能为空");
        }
        if (StringUtils.isNotBlank(title) && title.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (categoryId != null && categoryId > 0) {
            ArticleCat category = articleCatService.getById(categoryId);
            ThrowUtils.throwIf(category == null, ErrorCode.PARAMS_ERROR, "分类不存在");
        }
    }

    @Override
    public QueryWrapper<${upperDataKey}> getQueryWrapper(${upperDataKey}QueryRequest ${dataKey}QueryRequest) {
        QueryWrapper<${upperDataKey}> queryWrapper = new QueryWrapper<>();
        if (${dataKey}QueryRequest == null) {
            return queryWrapper;
        }
        Long id = ${dataKey}QueryRequest.getId();
        Long categoryId = ${dataKey}QueryRequest.getCategoryId();
        String title = ${dataKey}QueryRequest.getTitle();
        Integer status = ${dataKey}QueryRequest.getStatus();
        Integer isHome = ${dataKey}QueryRequest.getIsHome();
        Integer isTop = ${dataKey}QueryRequest.getIsTop();
        String sortField = ${dataKey}QueryRequest.getSortField();
        String sortOrder = ${dataKey}QueryRequest.getSortOrder();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(categoryId != null, "categoryId", categoryId);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.eq(isHome != null, "isHome", isHome);
        queryWrapper.eq(isTop != null, "isTop", isTop);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder), sortField);
        return queryWrapper;
    }

    @Override
    public ${upperDataKey}VO get${upperDataKey}VO(${upperDataKey} ${dataKey}) {
        if (${dataKey} == null) {
            return null;
        }
        ${upperDataKey}VO ${dataKey}VO = new ${upperDataKey}VO();
        BeanUtils.copyProperties(${dataKey}, ${dataKey}VO);
        if (${dataKey}.getCategoryId() != null) {
            ArticleCat category = articleCatService.getById(${dataKey}.getCategoryId());
            if (category != null) {
                ${dataKey}VO.setCategoryName(category.getName());
            }
        }
        return ${dataKey}VO;
    }

    @Override
    public Page<${upperDataKey}VO> get${upperDataKey}VOPage(Page<${upperDataKey}> ${dataKey}Page) {
        List<${upperDataKey}> ${dataKey}List = ${dataKey}Page.getRecords();
        Page<${upperDataKey}VO> ${dataKey}VOPage = new Page<>(${dataKey}Page.getCurrent(), ${dataKey}Page.getSize(),
                ${dataKey}Page.getTotal());
        if (CollUtil.isEmpty(${dataKey}List)) {
            return ${dataKey}VOPage;
        }
        Set<Long> categoryIdSet = ${dataKey}List.stream()
                .map(${upperDataKey}::getCategoryId)
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toSet());
        Map<Long, String> categoryNameMap = articleCatService.listByIds(categoryIdSet).stream()
                .collect(Collectors.toMap(ArticleCat::getId, ArticleCat::getName, (a, b) -> a));
        List<${upperDataKey}VO> ${dataKey}VOList = ${dataKey}List.stream().map(${dataKey} -> {
            ${upperDataKey}VO ${dataKey}VO = new ${upperDataKey}VO();
            BeanUtils.copyProperties(${dataKey}, ${dataKey}VO);
            if (${dataKey}.getCategoryId() != null) {
                ${dataKey}VO.setCategoryName(categoryNameMap.get(${dataKey}.getCategoryId()));
            }
            return ${dataKey}VO;
        }).collect(Collectors.toList());
        ${dataKey}VOPage.setRecords(${dataKey}VOList);
        return ${dataKey}VOPage;
    }
}
