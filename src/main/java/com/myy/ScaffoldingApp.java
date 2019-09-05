package com.myy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@MapperScan("com.myy.*.mapper")
public class ScaffoldingApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ScaffoldingApp.class)
                .run(args);
    }

}
