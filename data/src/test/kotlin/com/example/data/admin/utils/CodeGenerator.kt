package com.example.data.admin.utils

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.generator.FastAutoGenerator
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine
import java.io.File
import java.util.Properties

fun main() {
    // 1. 加载 application.properties
    val props = Properties()
    val configFile = File(System.getProperty("user.dir") + "/data/src/test/resources/application.properties")

    if (configFile.exists()) {
        props.load(configFile.inputStream())
    } else {
        throw RuntimeException("未找到配置文件: ${configFile.absolutePath}")
    }

    // 2. 获取配置值
    val url = props.getProperty("spring.datasource.url")
    val user = props.getProperty("spring.datasource.username")
    val pass = props.getProperty("spring.datasource.password")


    // 1. 基础配置：数据库连接
    FastAutoGenerator.create(
       url,user,pass
    )
        .globalConfig { builder ->
            builder.author("xkl")
                .enableKotlin() // 开启 Kotlin 模式
                .commentDate("yyyy-MM-dd")
                .outputDir(System.getProperty("user.dir") + "/data/src/main/kotlin/com/example/data/admin") // 输出到主目录
        }
        .packageConfig { builder ->
            builder.parent("com.example") // 父包名
                .moduleName("data") // 模块名，最终包名 com.example.entity
        }
        .strategyConfig { builder ->
            builder.addInclude("admin") // 包含的表名
                .addTablePrefix("t_", "sys_") // 去前缀

                // Entity 策略配置
                .entityBuilder()
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                .idType(IdType.AUTO) // PgSQL 常用自增或 Serial
                .enableTableFieldAnnotation() // 生成 @TableField 注解

                // Mapper 策略配置
                .mapperBuilder()
                .formatMapperFileName("%sMapper") // 如 UserMapper
                .enableBaseResultMap()

                // 核心：禁用 Service 和 ServiceImpl 的生成
                .serviceBuilder()
                .disable()

                // 如果你也不想要 Controller，同样可以禁用
                .controllerBuilder()
                .disable()
        }
        .templateEngine(VelocityTemplateEngine())
        .execute()
}
