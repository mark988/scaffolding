package com.myy.system.controller;

import Log;
import FebsUtil;
import FebsConstant;
import BaseController;
import FebsResponse;
import QueryRequest;
import FebsException;
import ${basePackage}.${entityPackage}.${className};
import ${basePackage}.${servicePackage}.I${className}Service;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * ${tableComment} Controller
 *
 * @author ${author}
 * @date ${date}
 */
@Slf4j
@Validated
@Controller
public class ${className}Controller extends BaseController {

    @Autowired
    private I${className}Service ${className?uncap_first}Service;

    @GetMapping(FebsConstant.VIEW_PREFIX + "${className?uncap_first}")
    private String ${className?uncap_first}Index(){
        return FebsUtil.view("${className?uncap_first}/${className?uncap_first}");
    }

    @GetMapping("${className?uncap_first}")
    @RequiresPermissions("${className?uncap_first}:list")
    public FebsResponse getAll${className}s(${className} ${className?uncap_first}) {
        return new FebsResponse().success().data(${className?uncap_first}Service.find${className}s(${className?uncap_first}));
    }

    @GetMapping("${className?uncap_first}/list")
    @RequiresPermissions("${className?uncap_first}:list")
    public FebsResponse ${className?uncap_first}List(QueryRequest request, ${className} ${className?uncap_first}) {
        Map<String, Object> dataTable = getDataTable(this.${className?uncap_first}Service.find${className}s(request, ${className?uncap_first}));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增${className}")
    @PostMapping("${className?uncap_first}")
    @RequiresPermissions("${className?uncap_first}:add")
    public FebsResponse add${className}(@Valid ${className} ${className?uncap_first}) throws FebsException {
        try {
            this.${className?uncap_first}Service.create${className}(${className?uncap_first});
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增${className}失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除${className}")
    @GetMapping("${className?uncap_first}/delete")
    @RequiresPermissions("${className?uncap_first}:delete")
    public FebsResponse delete${className}(${className} ${className?uncap_first}) throws FebsException {
        try {
            this.${className?uncap_first}Service.delete${className}(${className?uncap_first});
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除${className}失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改${className}")
    @PostMapping("${className?uncap_first}/update")
    @RequiresPermissions("${className?uncap_first}:update")
    public FebsResponse update${className}(${className} ${className?uncap_first}) throws FebsException {
        try {
            this.${className?uncap_first}Service.update${className}(${className?uncap_first});
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改${className}失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("${className?uncap_first}/excel")
    @RequiresPermissions("${className?uncap_first}:export")
    public void export(QueryRequest queryRequest, ${className} ${className?uncap_first}, HttpServletResponse response) throws FebsException {
        try {
            List<${className}> ${className?uncap_first}s = this.${className?uncap_first}Service.find${className}s(queryRequest, ${className?uncap_first}).getRecords();
            ExcelKit.$Export(${className}.class, response).downXlsx(${className?uncap_first}s, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
