package ${packageName}.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import ${packageName}.model.dto.${dataKey}.${upperDataKey}QueryRequest;
import ${packageName}.model.entity.Menu;
import ${packageName}.model.vo.${upperDataKey}VO;
import java.util.List;

/**
 * ${dataName}服务
 *
 */
public interface ${upperDataKey}Service extends IService<Menu> {

    /**
     * 校验数据
     */
    void valid${upperDataKey}(Menu ${dataKey}, boolean add);

    /**
     * 获取查询条件
     */
    QueryWrapper<Menu> getQueryWrapper(${upperDataKey}QueryRequest ${dataKey}QueryRequest);

    /**
     * 获取${dataName}封装
     */
    ${upperDataKey}VO get${upperDataKey}VO(Menu ${dataKey});

    /**
     * 分页获取${dataName}封装
     */
    Page<${upperDataKey}VO> get${upperDataKey}VOPage(Page<Menu> ${dataKey}Page);

    /**
     * 构建菜单树
     */
    List<${upperDataKey}VO> buildMenuTree(List<Menu> menuList);
}
