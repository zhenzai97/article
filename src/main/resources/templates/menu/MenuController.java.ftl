package ${packageName}.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${packageName}.annotation.AuthCheck;
import ${packageName}.common.BaseResponse;
import ${packageName}.common.ErrorCode;
import ${packageName}.common.ResultUtils;
import ${packageName}.constant.UserConstant;
import ${packageName}.exception.BusinessException;
import ${packageName}.exception.ThrowUtils;
import ${packageName}.model.dto.${dataKey}.${upperDataKey}AddRequest;
import ${packageName}.model.dto.${dataKey}.${upperDataKey}QueryRequest;
import ${packageName}.model.dto.${dataKey}.${upperDataKey}UpdateRequest;
import ${packageName}.model.entity.Menu;
import ${packageName}.model.entity.User;
import ${packageName}.model.vo.${upperDataKey}VO;
import ${packageName}.service.${upperDataKey}Service;
import ${packageName}.service.UserService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ${dataName}接口
 *
 */
@RestController
@RequestMapping("/${dataKey}")
@Slf4j
public class ${upperDataKey}Controller {

    @Resource
    private ${upperDataKey}Service ${dataKey}Service;

    @Resource
    private UserService userService;

    /**
     * 创建${dataName}
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> add${upperDataKey}(@RequestBody ${upperDataKey}AddRequest ${dataKey}AddRequest,
            HttpServletRequest request) {
        ThrowUtils.throwIf(${dataKey}AddRequest == null, ErrorCode.PARAMS_ERROR);
        Menu ${dataKey} = new Menu();
        BeanUtils.copyProperties(${dataKey}AddRequest, ${dataKey});
        ${dataKey}Service.valid${upperDataKey}(${dataKey}, true);
        User loginUser = userService.getLoginUser(request);
        ${dataKey}.setCreateUserId(loginUser.getId());
        if (${dataKey}.getParentId() == null) {
            ${dataKey}.setParentId(0L);
        }
        if (${dataKey}.getSort() == null) {
            ${dataKey}.setSort(0);
        }
        if (${dataKey}.getVisible() == null) {
            ${dataKey}.setVisible(1);
        }
        if (${dataKey}.getStatus() == null) {
            ${dataKey}.setStatus(1);
        }
        if (${dataKey}.getIsCache() == null) {
            ${dataKey}.setIsCache(0);
        }
        boolean result = ${dataKey}Service.save(${dataKey});
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(${dataKey}.getId());
    }

    /**
     * 删除${dataName}
     */
    @DeleteMapping("/delete/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> delete${upperDataKey}(@PathVariable("id") long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Menu old${upperDataKey} = ${dataKey}Service.getById(id);
        ThrowUtils.throwIf(old${upperDataKey} == null, ErrorCode.NOT_FOUND_ERROR);
        long childCount = ${dataKey}Service.count(new QueryWrapper<Menu>().eq("parentId", id));
        ThrowUtils.throwIf(childCount > 0, ErrorCode.OPERATION_ERROR, "存在子菜单，无法删除");
        boolean result = ${dataKey}Service.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新${dataName}
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> update${upperDataKey}(@RequestBody ${upperDataKey}UpdateRequest ${dataKey}UpdateRequest) {
        if (${dataKey}UpdateRequest == null || ${dataKey}UpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Menu old${upperDataKey} = ${dataKey}Service.getById(${dataKey}UpdateRequest.getId());
        ThrowUtils.throwIf(old${upperDataKey} == null, ErrorCode.NOT_FOUND_ERROR);
        Menu ${dataKey} = new Menu();
        BeanUtils.copyProperties(${dataKey}UpdateRequest, ${dataKey});
        ${dataKey}Service.valid${upperDataKey}(${dataKey}, false);
        boolean result = ${dataKey}Service.updateById(${dataKey});
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取${dataName}
     */
    @GetMapping("/get/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<${upperDataKey}VO> get${upperDataKey}VOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        Menu ${dataKey} = ${dataKey}Service.getById(id);
        ThrowUtils.throwIf(${dataKey} == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(${dataKey}Service.get${upperDataKey}VO(${dataKey}));
    }

    /**
     * 获取${dataName}树
     */
    @GetMapping("/list/tree/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<${upperDataKey}VO>> list${upperDataKey}TreeVO() {
        List<Menu> menuList = ${dataKey}Service.list(${dataKey}Service.getQueryWrapper(new ${upperDataKey}QueryRequest()));
        return ResultUtils.success(${dataKey}Service.buildMenuTree(menuList));
    }

    /**
     * 分页获取${dataName}列表
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Menu>> list${upperDataKey}ByPage(@RequestBody ${upperDataKey}QueryRequest ${dataKey}QueryRequest) {
        long current = ${dataKey}QueryRequest.getCurrent();
        long size = ${dataKey}QueryRequest.getPageSize();
        Page<Menu> ${dataKey}Page = ${dataKey}Service.page(new Page<>(current, size),
                ${dataKey}Service.getQueryWrapper(${dataKey}QueryRequest));
        return ResultUtils.success(${dataKey}Page);
    }

    /**
     * 分页获取${dataName}列表（封装类）
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<${upperDataKey}VO>> list${upperDataKey}VOByPage(
            @RequestBody ${upperDataKey}QueryRequest ${dataKey}QueryRequest) {
        long current = ${dataKey}QueryRequest.getCurrent();
        long size = ${dataKey}QueryRequest.getPageSize();
        Page<Menu> ${dataKey}Page = ${dataKey}Service.page(new Page<>(current, size),
                ${dataKey}Service.getQueryWrapper(${dataKey}QueryRequest));
        return ResultUtils.success(${dataKey}Service.get${upperDataKey}VOPage(${dataKey}Page));
    }
}
