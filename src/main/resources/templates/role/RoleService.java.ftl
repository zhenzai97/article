package ${packageName}.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import ${packageName}.model.dto.${dataKey}.${upperDataKey}QueryRequest;
import ${packageName}.model.entity.Role;
import ${packageName}.model.vo.${upperDataKey}VO;

/**
 * ${dataName}服务
 *
 */
public interface ${upperDataKey}Service extends IService<Role> {

    /**
     * 校验数据
     *
     * @param ${dataKey}
     * @param add  对创建的数据进行校验
     */
    void valid${upperDataKey}(Role ${dataKey}, boolean add);

    /**
     * 获取查询条件
     *
     * @param ${dataKey}QueryRequest
     * @return
     */
    QueryWrapper<Role> getQueryWrapper(${upperDataKey}QueryRequest ${dataKey}QueryRequest);

    /**
     * 获取${dataName}封装
     *
     * @param ${dataKey}
     * @return
     */
    ${upperDataKey}VO get${upperDataKey}VO(Role ${dataKey});

    /**
     * 分页获取${dataName}封装
     *
     * @param ${dataKey}Page
     * @return
     */
    Page<${upperDataKey}VO> get${upperDataKey}VOPage(Page<Role> ${dataKey}Page);
}
