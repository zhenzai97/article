package com.springbootinit.generate;

import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 代码生成器
 *
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
public class CodeGenerator {

    /**
     * 用法：修改生成参数和生成路径，注释掉不需要的生成逻辑，然后运行即可
     *
     * @param args
     * @throws TemplateException
     * @throws IOException
     */
    public static void main(String[] args) throws TemplateException, IOException {
//        generateRole();
        generateMenu();
    }

    /**
     * 生成角色（role）模块代码
     */
    public static void generateRole() throws TemplateException, IOException {
        String packageName = "com.springbootinit";
        String dataName = "角色";
        String dataKey = "role";
        String upperDataKey = "Role";
        String templateDir = "role";

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageName);
        dataModel.put("dataName", dataName);
        dataModel.put("dataKey", dataKey);
        dataModel.put("upperDataKey", upperDataKey);

        String projectPath = System.getProperty("user.dir");
        String templateBase = projectPath + File.separator + "src/main/resources/templates/" + templateDir;

        // 1、Entity
        String inputPath = templateBase + "/Role.java.ftl";
        String outputPath = String.format("%s/generator/model/entity/%s.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Entity 成功：" + outputPath);

        // 2、Mapper
        inputPath = templateBase + "/RoleMapper.java.ftl";
        outputPath = String.format("%s/generator/mapper/%sMapper.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Mapper 成功：" + outputPath);

        // 3、Controller
        inputPath = templateBase + "/RoleController.java.ftl";
        outputPath = String.format("%s/generator/controller/%sController.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Controller 成功：" + outputPath);

        // 4、Service 接口和实现类
        inputPath = templateBase + "/RoleService.java.ftl";
        outputPath = String.format("%s/generator/service/%sService.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Service 接口成功：" + outputPath);

        inputPath = templateBase + "/RoleServiceImpl.java.ftl";
        outputPath = String.format("%s/generator/service/impl/%sServiceImpl.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Service 实现类成功：" + outputPath);

        // 5、DTO 和 VO
        inputPath = templateBase + "/RoleAddRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sAddRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);

        inputPath = templateBase + "/RoleQueryRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sQueryRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);

        inputPath = templateBase + "/RoleEditRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sEditRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);

        inputPath = templateBase + "/RoleUpdateRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sUpdateRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 DTO 成功");

        inputPath = templateBase + "/RoleVO.java.ftl";
        outputPath = String.format("%s/generator/model/vo/%sVO.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 VO 成功：" + outputPath);

        System.out.println("角色模块代码已全部生成到 generator/ 目录，请移动到 src/main/java 对应包路径");
    }


    /**
     * 生成菜单（menu）模块代码
     */
    public static void generateMenu() throws TemplateException, IOException {
        String packageName = "com.springbootinit";
        String dataName = "菜单";
        String dataKey = "menu";
        String upperDataKey = "Menu";
        String templateDir = "menu";

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageName);
        dataModel.put("dataName", dataName);
        dataModel.put("dataKey", dataKey);
        dataModel.put("upperDataKey", upperDataKey);

        String projectPath = System.getProperty("user.dir");
        String templateBase = projectPath + File.separator + "src/main/resources/templates/" + templateDir;

        // 1、Entity
        String inputPath = templateBase + "/Menu.java.ftl";
        String outputPath = String.format("%s/generator/model/entity/%s.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Entity 成功：" + outputPath);

        // 2、Mapper
        inputPath = templateBase + "/MenuMapper.java.ftl";
        outputPath = String.format("%s/generator/mapper/%sMapper.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Mapper 成功：" + outputPath);

        // 3、Controller
        inputPath = templateBase + "/MenuController.java.ftl";
        outputPath = String.format("%s/generator/controller/%sController.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Controller 成功：" + outputPath);

        // 4、Service 接口和实现类
        inputPath = templateBase + "/MenuService.java.java.ftl";
        outputPath = String.format("%s/generator/service/%sService.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Service 接口成功：" + outputPath);

        inputPath = templateBase + "/MenuServiceImpl.java.ftl";
        outputPath = String.format("%s/generator/service/impl/%sServiceImpl.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Service 实现类成功：" + outputPath);

        // 5、DTO 和 VO
        inputPath = templateBase + "/MenuAddRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sAddRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);

        inputPath = templateBase + "/MenuQueryRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sQueryRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);

        inputPath = templateBase + "/MenuEditRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sEditRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);

        inputPath = templateBase + "/MenuUpdateRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sUpdateRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 DTO 成功");

        inputPath = templateBase + "/MenuVO.java.ftl";
        outputPath = String.format("%s/generator/model/vo/%sVO.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 VO 成功：" + outputPath);

        System.out.println("菜单模块代码已全部生成到 generator/ 目录，请移动到 src/main/java 对应包路径");
    }
    /**
     * 生成文件
     *
     * @param inputPath  模板文件输入路径
     * @param outputPath 输出路径
     * @param model      数据模型
     * @throws IOException
     * @throws TemplateException
     */
    public static void doGenerate(String inputPath, String outputPath, Object model) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        File templateFile = new File(inputPath);
        if (!templateFile.exists()) {
            throw new IOException("模板文件不存在: " + inputPath);
        }
        File templateDir = templateFile.getParentFile();
        configuration.setDirectoryForTemplateLoading(templateDir);
        configuration.setDefaultEncoding("utf-8");
        String templateName = templateFile.getName();
        Template template = configuration.getTemplate(templateName);
        if (!FileUtil.exist(outputPath)) {
            FileUtil.touch(outputPath);
        }
        // 必须使用 UTF-8 写入，避免 Windows 默认 GBK 导致中文注释乱码
        Writer out = new OutputStreamWriter(new FileOutputStream(outputPath), StandardCharsets.UTF_8);
        template.process(model, out);
        out.close();
    }
}
