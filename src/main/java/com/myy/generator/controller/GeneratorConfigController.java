package com.myy.generator.controller;

import com.myy.common.annotation.Log;
import com.myy.common.controller.BaseController;
import com.myy.common.entity.FebsResponse;
import com.myy.common.exception.FebsException;
import com.myy.generator.entity.GeneratorConfig;
import com.myy.generator.servie.IGeneratorConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author mark
 */
@Slf4j
@RestController
@RequestMapping("generatorConfig")
public class GeneratorConfigController extends BaseController {

    @Autowired
    private IGeneratorConfigService generatorConfigService;

    @GetMapping
    @RequiresPermissions("generator:configure:view")
    public FebsResponse getGeneratorConfig() {
        return new FebsResponse().success().data(generatorConfigService.findGeneratorConfig());
    }

    @Log("修改GeneratorConfig")
    @PostMapping("update")
    @RequiresPermissions("generator:configure:update")
    public FebsResponse updateGeneratorConfig(@Valid GeneratorConfig generatorConfig) throws FebsException {
        try {
            if (StringUtils.isBlank(generatorConfig.getId()))
                throw new FebsException("配置id不能为空");
            this.generatorConfigService.updateGeneratorConfig(generatorConfig);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改GeneratorConfig失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
