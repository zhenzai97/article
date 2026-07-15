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
        // generateMenu();
        // generateArticleCategory();
        // generateArticle();
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
     * 生成文章分类（articleCat）模块代码
     * <p>
     * 对应前端 ArticleCatAddRequestData：sign、name、sort、status、remark
     */
    public static void generateArticleCategory() throws TemplateException, IOException {
        String packageName = "com.springbootinit";
        String dataName = "文章分类";
        String dataKey = "articleCat";
        String upperDataKey = "ArticleCat";
        String tableName = "article_category";
        String templateDir = "articleCat";

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageName);
        dataModel.put("dataName", dataName);
        dataModel.put("dataKey", dataKey);
        dataModel.put("upperDataKey", upperDataKey);
        dataModel.put("tableName", tableName);

        String projectPath = System.getProperty("user.dir");
        String templateBase = projectPath + File.separator + "src/main/resources/templates/" + templateDir;

        // 1、Entity
        String inputPath = templateBase + "/ArticleCat.java.ftl";
        String outputPath = String.format("%s/generator/model/entity/%s.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Entity 成功：" + outputPath);

        // 2、Mapper
        inputPath = templateBase + "/ArticleCatMapper.java.ftl";
        outputPath = String.format("%s/generator/mapper/%sMapper.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Mapper 成功：" + outputPath);

        // 3、Controller
        inputPath = templateBase + "/ArticleCatController.java.ftl";
        outputPath = String.format("%s/generator/controller/%sController.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Controller 成功：" + outputPath);

        // 4、Service 接口和实现类
        inputPath = templateBase + "/ArticleCatService.java.ftl";
        outputPath = String.format("%s/generator/service/%sService.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Service 接口成功：" + outputPath);

        inputPath = templateBase + "/ArticleCatServiceImpl.java.ftl";
        outputPath = String.format("%s/generator/service/impl/%sServiceImpl.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Service 实现类成功：" + outputPath);

        // 5、DTO 和 VO
        inputPath = templateBase + "/ArticleCatAddRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sAddRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);

        inputPath = templateBase + "/ArticleCatQueryRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sQueryRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);

        inputPath = templateBase + "/ArticleCatEditRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sEditRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);

        inputPath = templateBase + "/ArticleCatUpdateRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sUpdateRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 DTO 成功");

        inputPath = templateBase + "/ArticleCatVO.java.ftl";
        outputPath = String.format("%s/generator/model/vo/%sVO.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 VO 成功：" + outputPath);

        System.out.println("文章分类模块代码已全部生成到 generator/ 目录，请移动到 src/main/java 对应包路径");
        System.out.println("请先在数据库执行 sql/create_table.sql 中的 article_category 建表语句");
    }

    /**
     * 生成文章（article）模块代码
     * <p>
     * 对应旧系统 JSON 字段映射：
     * title、content、cover、video、sort、read→readNum、status、is_home→isHome、is_top→isTop、cate_id→categoryId
     * 丢弃：_id、cate_id 嵌套对象、_createTime、_updateTime（入库时用数据库时间或迁移脚本转换）
     */
    public static void generateArticle() throws TemplateException, IOException {
        String packageName = "com.springbootinit";
        String dataName = "文章";
        String dataKey = "article";
        String upperDataKey = "Article";
        String tableName = "article";
        String templateDir = "article";

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageName);
        dataModel.put("dataName", dataName);
        dataModel.put("dataKey", dataKey);
        dataModel.put("upperDataKey", upperDataKey);
        dataModel.put("tableName", tableName);

        String projectPath = System.getProperty("user.dir");
        String templateBase = projectPath + File.separator + "src/main/resources/templates/" + templateDir;

        String inputPath = templateBase + "/Article.java.ftl";
        String outputPath = String.format("%s/generator/model/entity/%s.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Entity 成功：" + outputPath);

        inputPath = templateBase + "/ArticleMapper.java.ftl";
        outputPath = String.format("%s/generator/mapper/%sMapper.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Mapper 成功：" + outputPath);

        inputPath = templateBase + "/ArticleController.java.ftl";
        outputPath = String.format("%s/generator/controller/%sController.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Controller 成功：" + outputPath);

        inputPath = templateBase + "/ArticleService.java.ftl";
        outputPath = String.format("%s/generator/service/%sService.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Service 接口成功：" + outputPath);

        inputPath = templateBase + "/ArticleServiceImpl.java.ftl";
        outputPath = String.format("%s/generator/service/impl/%sServiceImpl.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Service 实现类成功：" + outputPath);

        inputPath = templateBase + "/ArticleAddRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sAddRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);

        inputPath = templateBase + "/ArticleQueryRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sQueryRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);

        inputPath = templateBase + "/ArticleEditRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sEditRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);

        inputPath = templateBase + "/ArticleUpdateRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sUpdateRequest.java", projectPath, dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 DTO 成功");

        inputPath = templateBase + "/ArticleVO.java.ftl";
        outputPath = String.format("%s/generator/model/vo/%sVO.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 VO 成功：" + outputPath);

        moveGeneratedModule(projectPath, dataKey, upperDataKey);
        System.out.println("文章模块代码已移动到 src/main/java 对应包路径");
        System.out.println("请先在数据库执行 sql/create_table.sql 中的 article 建表语句");
    }

    /**
     * 将 generator/ 下生成的模块代码移动到 src/main/java
     */
    public static void moveGeneratedModule(String projectPath, String dataKey, String upperDataKey) throws IOException {
        String srcBase = projectPath + "/src/main/java/com/springbootinit";
        String genBase = projectPath + "/generator";

        moveFile(genBase + "/controller/" + upperDataKey + "Controller.java",
                srcBase + "/controller/" + upperDataKey + "Controller.java");
        moveFile(genBase + "/mapper/" + upperDataKey + "Mapper.java",
                srcBase + "/mapper/" + upperDataKey + "Mapper.java");
        moveFile(genBase + "/service/" + upperDataKey + "Service.java",
                srcBase + "/service/" + upperDataKey + "Service.java");
        moveFile(genBase + "/service/impl/" + upperDataKey + "ServiceImpl.java",
                srcBase + "/service/impl/" + upperDataKey + "ServiceImpl.java");
        moveFile(genBase + "/model/entity/" + upperDataKey + ".java",
                srcBase + "/model/entity/" + upperDataKey + ".java");
        moveFile(genBase + "/model/vo/" + upperDataKey + "VO.java",
                srcBase + "/model/vo/" + upperDataKey + "VO.java");
        moveFile(genBase + "/model/dto/" + dataKey + "/" + upperDataKey + "AddRequest.java",
                srcBase + "/model/dto/" + dataKey + "/" + upperDataKey + "AddRequest.java");
        moveFile(genBase + "/model/dto/" + dataKey + "/" + upperDataKey + "QueryRequest.java",
                srcBase + "/model/dto/" + dataKey + "/" + upperDataKey + "QueryRequest.java");
        moveFile(genBase + "/model/dto/" + dataKey + "/" + upperDataKey + "EditRequest.java",
                srcBase + "/model/dto/" + dataKey + "/" + upperDataKey + "EditRequest.java");
        moveFile(genBase + "/model/dto/" + dataKey + "/" + upperDataKey + "UpdateRequest.java",
                srcBase + "/model/dto/" + dataKey + "/" + upperDataKey + "UpdateRequest.java");
    }

    private static void moveFile(String source, String target) throws IOException {
        File sourceFile = new File(source);
        if (!sourceFile.exists()) {
            throw new IOException("生成文件不存在: " + source);
        }
        File targetFile = new File(target);
        File parent = targetFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        FileUtil.copy(sourceFile, targetFile, true);
        System.out.println("移动文件：" + target);
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
