package com.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.mapper.RoleMenuMapper;
import com.springbootinit.model.entity.RoleMenu;
import com.springbootinit.service.RoleMenuService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    public List<Long> listMenuIdsByRoleId(Long roleId) {
        if (roleId == null) {
            return new ArrayList<>();
        }
        List<RoleMenu> list = this.list(new QueryWrapper<RoleMenu>().eq("roleId", roleId));
        return list.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenus(Long roleId, List<Long> menuIds, Long createUserId) {
        if (roleId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        removeByRoleId(roleId);
        if (CollUtil.isEmpty(menuIds)) {
            return;
        }
        Date now = new Date();
        List<RoleMenu> rows = menuIds.stream()
                .filter(id -> id != null)
                .distinct()
                .map(menuId -> {
                    RoleMenu rm = new RoleMenu();
                    rm.setRoleId(roleId);
                    rm.setMenuId(menuId);
                    rm.setCreateUserId(createUserId);
                    rm.setCreateTime(now);
                    return rm;
                })
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(rows)) {
            this.saveBatch(rows);
        }
    }

    @Override
    public void removeByRoleId(Long roleId) {
        if (roleId == null) {
            return;
        }
        this.remove(new QueryWrapper<RoleMenu>().eq("roleId", roleId));
    }
}
