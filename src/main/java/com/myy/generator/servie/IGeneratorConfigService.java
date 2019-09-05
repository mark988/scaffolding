package com.myy.generator.servie;

import com.myy.generator.entity.GeneratorConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author mark
 */
public interface IGeneratorConfigService extends IService<GeneratorConfig> {

    /**
     * 查询
     *
     * @return GeneratorConfig
     */
    GeneratorConfig findGeneratorConfig();

    /**
     * 修改
     *
     * @param generatorConfig generatorConfig
     */
    void updateGeneratorConfig(GeneratorConfig generatorConfig);

}
