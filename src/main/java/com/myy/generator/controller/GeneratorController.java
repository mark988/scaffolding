package com.myy.generator.controller;

import com.myy.common.controller.BaseController;
import com.myy.common.entity.FebsResponse;
import com.myy.common.entity.QueryRequest;
import com.myy.common.exception.FebsException;
import com.myy.common.exception.FileDownloadException;
import com.myy.common.utils.FebsUtil;
import com.myy.common.utils.FileUtil;
import com.myy.generator.entity.Column;
import com.myy.generator.entity.GeneratorConfig;
import com.myy.generator.entity.GeneratorConstant;
import com.myy.generator.helper.GeneratorHelper;
import com.myy.generator.servie.IGeneratorConfigService;
import com.myy.generator.servie.IGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("generator")
public class GeneratorController extends BaseController {

    private static final String SUFFIX = "_code.zip";

    @Autowired
    private IGeneratorService generatorService;
    @Autowired
    private IGeneratorConfigService generatorConfigService;
    @Autowired
    private GeneratorHelper generatorHelper;

    @GetMapping("tables/info")
    @RequiresPermissions("generator:view")
    public FebsResponse tablesInfo(String tableName, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(generatorService.getTables(tableName, request, GeneratorConstant.DATABASE_TYPE, GeneratorConstant.DATABASE_NAME));
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping
    @RequiresPermissions("generator:generate")
    public void generate(@NotBlank(message = "{required}") String name, String remark, HttpServletResponse response) throws FileDownloadException {
        try {
            GeneratorConfig generatorConfig = generatorConfigService.findGeneratorConfig();
            if (generatorConfig == null) {
                throw new FebsException("代码生成配置为空");
            }

            String className = name;
            if (GeneratorConfig.TRIM_YES.equals(generatorConfig.getIsTrim())) {
                className = name.replace(generatorConfig.getTrimValue(), "");
            }

            generatorConfig.setTableName(name);
            generatorConfig.setClassName(FebsUtil.underscoreToCamel(className));
            generatorConfig.setTableComment(remark);
            // 生成代码到临时目录
            List<Column> columns = generatorService.getColumns(GeneratorConstant.DATABASE_TYPE, GeneratorConstant.DATABASE_NAME, name);
            generatorHelper.generateEntityFile(columns, generatorConfig);
            generatorHelper.generateMapperFile(columns, generatorConfig);
            generatorHelper.generateMapperXmlFile(columns, generatorConfig);
            generatorHelper.generateServiceFile(columns, generatorConfig);
            generatorHelper.generateServiceImplFile(columns, generatorConfig);
            generatorHelper.generateControllerFile(columns, generatorConfig);
            // 打包
            String zipFile = System.currentTimeMillis() + SUFFIX;
            FileUtil.compress(GeneratorConstant.TEMP_PATH + "src", zipFile);
            // 下载
            FileUtil.download(zipFile, name + SUFFIX, true, response);
            // 删除临时目录
            FileUtil.delete(GeneratorConstant.TEMP_PATH);
        } catch (Exception e) {
            String message = "代码生成失败，" + e.getMessage();
            log.error(message, e);
            throw new FileDownloadException(message);
        }
    }
}
