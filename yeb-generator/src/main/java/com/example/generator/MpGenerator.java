package com.example.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MpGenerator {
    public static void main(String[] args) {
        String tables="t_admin,t_admin_role,t_appraise,t_department,t_employee,t_employee_ec,t_employee_remove,t_employee_train,t_joblevel,t_mail_log,t_menu,t_menu_role,t_nation,t_oplog,t_politics_status,t_position,t_role,t_salary,t_salary_adjust,t_sys_msg,t_sys_msg_content";
        String[] tablesNames=tables.split(",");
        List<String> tableList = new ArrayList<>(Arrays.asList(tablesNames));


//        首先还是需要配置
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/yeb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai",
                "root","yxw660786")
                .globalConfig(builder -> {
                    builder.author("dys")
                            .outputDir(System.getProperty("user.dir")+"/yeb-generator/src/main/java")
                            .disableOpenDir()
                            .enableSwagger()
                            .commentDate("yyyy-MM-dd");

                })
                .packageConfig(builder -> {
                    builder.parent("com.example")
                            .moduleName("generator")
                            .entity("pojo")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml,System.getProperty("user.dir")+"/yeb-generator/src/main/resources/mapper"));
                })
                .strategyConfig((scanner,builder)->{
                    builder.addInclude(tableList)
                            .addTablePrefix("t_")
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .entityBuilder()
                            .enableLombok()
                            .enableChainModel()
                            .logicDeleteColumnName("deleted")
                            .enableTableFieldAnnotation()
                            .naming(NamingStrategy.underline_to_camel)
                            .columnNaming(NamingStrategy.no_change)
                            .controllerBuilder()
                            .enableHyphenStyle()
                            .formatFileName("%sController")
                            .enableRestStyle()
                            .mapperBuilder()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation()
                            .formatXmlFileName("%sMapper");
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
