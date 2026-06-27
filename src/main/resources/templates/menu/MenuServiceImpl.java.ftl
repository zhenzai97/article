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
import ${packageName}.model.entity.Menu;
import ${packageName}.model.vo.${upperDataKey}VO;
import ${packageName}.service.${upperDataKey}Service;
import ${packageName}.utils.SqlUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * ${dataName}服务实现
 *
 */
@Service
public class ${upperDataKey}ServiceImpl extends ServiceImpl<${upperDataKey}Mapper, Menu> implements ${upperDataKey}Service {

    @Override
    public void valid${upperDataKey}(Menu ${dataKey}, boolean add) {
        ThrowUtils.throwIf(${dataKey} == null, ErrorCode.PARAMS_ERROR);
        String menuName = ${dataKey}.getMenuName();
        Integer menuType = ${dataKey}.getMenuType();
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(menuName), ErrorCode.PARAMS_ERROR, "菜单名称不能为空");
            ThrowUtils.throwIf(menuType == null, ErrorCode.PARAMS_ERROR, "菜单类型不能为空");
        }
        if (StringUtils.isNotBlank(menuName) && menuName.length() > 128) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "菜单名称过长");
        }
        if (menuType != null && (menuType < 1 || menuType > 3)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "菜单类型无效");
        }
    }

    @Override
    public QueryWrapper<Menu> getQueryWrapper(${upperDataKey}QueryRequest ${dataKey}QueryRequest) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (${dataKey}QueryRequest == null) {
            return queryWrapper;
        }
        Long id = ${dataKey}QueryRequest.getId();
        Long parentId = ${dataKey}QueryRequest.getParentId();
        String menuName = ${dataKey}QueryRequest.getMenuName();
        Integer menuType = ${dataKey}QueryRequest.getMenuType();
        Integer visible = ${dataKey}QueryRequest.getVisible();
        Integer status = ${dataKey}QueryRequest.getStatus();
        String sortField = ${dataKey}QueryRequest.getSortField();
        String sortOrder = ${dataKey}QueryRequest.getSortOrder();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(parentId != null, "parentId", parentId);
        queryWrapper.like(StringUtils.isNotBlank(menuName), "menuName", menuName);
        queryWrapper.eq(menuType != null, "menuType", menuType);
        queryWrapper.eq(visible != null, "visible", visible);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder), sortField);
        if (!SqlUtils.validSortField(sortField)) {
            queryWrapper.orderByAsc("sort");
        }
        return queryWrapper;
    }

    @Override
    public ${upperDataKey}VO get${upperDataKey}VO(Menu ${dataKey}) {
        if (${dataKey} == null) {
            return null;
        }
        ${upperDataKey}VO ${dataKey}VO = new ${upperDataKey}VO();
        BeanUtils.copyProperties(${dataKey}, ${dataKey}VO);
        return ${dataKey}VO;
    }

    @Override
    public Page<${upperDataKey}VO> get${upperDataKey}VOPage(Page<Menu> ${dataKey}Page) {
        List<Menu> ${dataKey}List = ${dataKey}Page.getRecords();
        Page<${upperDataKey}VO> ${dataKey}VOPage = new Page<>(${dataKey}Page.getCurrent(), ${dataKey}Page.getSize(),
                ${dataKey}Page.getTotal());
        if (CollUtil.isEmpty(${dataKey}List)) {
            return ${dataKey}VOPage;
        }
        List<${upperDataKey}VO> ${dataKey}VOList = ${dataKey}List.stream().map(this::get${upperDataKey}VO)
                .collect(Collectors.toList());
        ${dataKey}VOPage.setRecords(${dataKey}VOList);
        return ${dataKey}VOPage;
    }

    @Override
    public List<${upperDataKey}VO> buildMenuTree(List<Menu> menuList) {
        if (CollUtil.isEmpty(menuList)) {
            return new ArrayList<>();
        }
        List<${upperDataKey}VO> voList = menuList.stream().map(this::get${upperDataKey}VO).collect(Collectors.toList());
        Map<Long, List<${upperDataKey}VO>> parentMap = voList.stream()
                .collect(Collectors.groupingBy(vo -> vo.getParentId() == null ? 0L : vo.getParentId()));
        voList.forEach(vo -> vo.setChildren(parentMap.get(vo.getId())));
        return parentMap.getOrDefault(0L, new ArrayList<>());
    }
}
