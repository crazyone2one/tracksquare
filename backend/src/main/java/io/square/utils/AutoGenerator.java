package io.square.utils;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;

/**
 * @author by 11's papa on 2022年06月13日
 * @version 1.0.0
 */
public class AutoGenerator {

    public static void main(String[] args) {
        String path = System.getProperty("user.dir") + "\\backend";
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/track-square","root","admin")
                .globalConfig(builder -> {
                    builder.outputDir(path + "/src/main/java")
                            .author("11's papa")
                            .dateType(DateType.TIME_PACK)
                            .commentDate("yyyy-MM-dd")
                            .disableOpenDir()
                            .build();
                })
                .packageConfig(builder -> {
                    builder.parent("io.square")
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("mapper")
                            .xml("mapper")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, path+"/src/main/resources/mapper")).build();
                })
                .strategyConfig(builder -> {
                    builder.addInclude("group").addTablePrefix("")
                            .serviceBuilder().formatServiceFileName("%sService").formatServiceImplFileName("%sServiceImpl")
                            .entityBuilder().enableLombok().logicDeleteColumnName("deleted").enableTableFieldAnnotation().idType(IdType.ASSIGN_UUID)
                            .controllerBuilder().formatFileName("%sController").enableRestStyle()
                            .mapperBuilder().enableBaseResultMap().superClass(BaseMapper.class).formatMapperFileName("%sMapper").enableMapperAnnotation().formatXmlFileName("%sMapper")
                            .build();
                }).templateEngine(new VelocityTemplateEngine())
                .execute();
    }
}
