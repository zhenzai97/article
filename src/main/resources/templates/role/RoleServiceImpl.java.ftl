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
import ${packageName}.model.entity.Role;
import ${packageName}.model.vo.${upperDataKey}VO;
import ${packageName}.service.${upperDataKey}Service;
import ${packageName}.utils.SqlUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * ${dataName}服务实现
 *
 */
@Service
public class ${upperDataKey}ServiceImpl extends ServiceImpl<${upperDataKey}Mapper, Role> implements ${upperDataKey}Service {

    @Override
    public void valid${upperDataKey}(Role ${dataKey}, boolean add) {
        ThrowUtils.throwIf(${dataKey} == null, ErrorCode.PARAMS_ERROR);
        String roleCode = ${dataKey}.getRoleCode();
        String roleName = ${dataKey}.getRoleName();
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(roleCode, roleName), ErrorCode.PARAMS_ERROR, "角色编码和名称不能为空");
        }
        if (StringUtils.isNotBlank(roleCode) && roleCode.length() > 64) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "角色编码过长");
        }
        if (StringUtils.isNotBlank(roleName) && roleName.length() > 128) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "角色名称过长");
        }
    }

    @Override
    public QueryWrapper<Role> getQueryWrapper(${upperDataKey}QueryRequest ${dataKey}QueryRequest) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (${dataKey}QueryRequest == null) {
            return queryWrapper;
        }
        Long id = ${dataKey}QueryRequest.getId();
        String roleCode = ${dataKey}QueryRequest.getRoleCode();
        String roleName = ${dataKey}QueryRequest.getRoleName();
        Integer status = ${dataKey}QueryRequest.getStatus();
        String sortField = ${dataKey}QueryRequest.getSortField();
        String sortOrder = ${dataKey}QueryRequest.getSortOrder();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(roleCode), "roleCode", roleCode);
        queryWrapper.like(StringUtils.isNotBlank(roleName), "roleName", roleName);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder), sortField);
        return queryWrapper;
    }

    @Override
    public ${upperDataKey}VO get${upperDataKey}VO(Role ${dataKey}) {
        if (${dataKey} == null) {
            return null;
        }
        ${upperDataKey}VO ${dataKey}VO = new ${upperDataKey}VO();
        BeanUtils.copyProperties(${dataKey}, ${dataKey}VO);
        return ${dataKey}VO;
    }

    @Override
    public Page<${upperDataKey}VO> get${upperDataKey}VOPage(Page<Role> ${dataKey}Page) {
        List<Role> ${dataKey}List = ${dataKey}Page.getRecords();
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
}
