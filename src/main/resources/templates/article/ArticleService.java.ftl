package ${packageName}.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import ${packageName}.model.dto.${dataKey}.${upperDataKey}QueryRequest;
import ${packageName}.model.entity.${upperDataKey};
import ${packageName}.model.vo.${upperDataKey}VO;

/**
 * ${dataName}服务
 *
 */
public interface ${upperDataKey}Service extends IService<${upperDataKey}> {

    void valid${upperDataKey}(${upperDataKey} ${dataKey}, boolean add);

    QueryWrapper<${upperDataKey}> getQueryWrapper(${upperDataKey}QueryRequest ${dataKey}QueryRequest);

    ${upperDataKey}VO get${upperDataKey}VO(${upperDataKey} ${dataKey});

    Page<${upperDataKey}VO> get${upperDataKey}VOPage(Page<${upperDataKey}> ${dataKey}Page);
}
