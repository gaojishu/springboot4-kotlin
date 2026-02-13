package com.example.generator.core

import com.baomidou.mybatisplus.generator.FastAutoGenerator
import com.baomidou.mybatisplus.generator.config.OutputFile
import com.baomidou.mybatisplus.generator.config.PackageConfig
import com.baomidou.mybatisplus.generator.config.StrategyConfig
import com.baomidou.mybatisplus.generator.config.rules.IColumnType
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine
import com.example.generator.config.GeneratorProps
import com.example.generator.dto.req.GeneratorReq
import java.sql.Connection
import java.sql.DriverManager

class GeneratorEngine(
    private val config: GeneratorProps,
    private val params: GeneratorReq
) {

    fun execute() {
        checkTableExists()
        FastAutoGenerator.create(config.url, config.user, config.pass)
            .dataSourceConfig { builder ->
                builder.schema(params.schema)
                builder.typeConvertHandler { _, typeRegistry, metaInfo ->
                    if (metaInfo.typeName.contains("timestamptz")) {
                        return@typeConvertHandler object : IColumnType {
                            override fun getType() = "OffsetDateTime"
                            override fun getPkg() = "java.time.OffsetDateTime"
                        }
                    }
                    typeRegistry.getColumnType(metaInfo)
                }
            }
            .globalConfig { builder ->
                builder.author("xkl").enableKotlin().commentDate("yyyy-MM-dd")
                    .outputDir(config.rootPath).disableOpenDir()
            }
            .packageConfig { builder -> configurePackages(builder) }
            .strategyConfig { builder -> configureStrategy(builder) }
            .templateEngine(VelocityTemplateEngine())
            .execute()
    }

    private fun configurePackages(builder: PackageConfig.Builder) {
        val root = config.rootPath
        builder.parent("")
            .entity(params.entityPack)
            .mapper(params.mapperPack)
            .service(params.servicePack)
            .serviceImpl(params.serviceImplPack)
            .xml(params.mapperXmlPack)
            .controller(params.controllerPack)
            .pathInfo(mapOf(
                OutputFile.entity to "$root${params.entityPath}",
                OutputFile.mapper to "$root${params.mapperPath}",
                OutputFile.xml to "$root${params.mapperXmlPath}",
                OutputFile.service to "$root${params.servicePath}",
                OutputFile.serviceImpl to "$root${params.serviceImplPath}",
                OutputFile.controller to "$root${params.controllerPath}"
            ))
    }

    private fun configureStrategy(builder: StrategyConfig.Builder) {
        builder.addInclude(params.tables)
            .addTablePrefix(config.dbPrefix) // 去前缀

        // 1. Entity 配置
        val entity = builder.entityBuilder()
        entity.kotlinTemplatePath("/templates/vm/entity.kt.vm") // 设置实体类模板
        entity.disableSerialVersionUID()
        entity.formatFileName("%sEntity1")
        entity.naming(NamingStrategy.underline_to_camel)
        entity.columnNaming(NamingStrategy.underline_to_camel)
        entity.superClass("com.example.data.admin.entity.BaseEntity") // 设置父类的全类名
        entity.addIgnoreColumns("id", "created_at", "updated_at")
        params.entity ?: entity.disable()

        // 2.Mapper 配置
        val mapper = builder.mapperBuilder()
        mapper.mapperTemplate("/templates/vm/mapper.kt.vm")
        mapper.formatMapperFileName("%sMapper1") // 如 UserMapper
        mapper.enableBaseResultMap() //xml
        mapper.superClass("com.github.yulichang.base.MPJBaseMapper")
        params.mapper ?: mapper.disable()
        params.mapperXml ?: mapper.disableMapperXml()

        // 3.Service 配置
        val service = builder.serviceBuilder()
        service.serviceTemplate("/templates/vm/service.kt.vm")
        service.serviceImplTemplate("/templates/vm/service-impl.kt.vm")
        service.formatServiceFileName("%sService1")      // 去掉默认的 I 前缀 (如果你不喜欢 IUserService)
        service.formatServiceImplFileName("%sServiceImpl1")
        service.mapperBuilder()
        params.service ?: service.disableService()
        params.serviceImpl ?: service.disableServiceImpl()

        // 4. Controller 配置
        val controller = builder.controllerBuilder()
        controller.template("/templates/vm/controller.kt.vm")
        controller.enableRestStyle()
        controller.formatFileName("%sController1")
        params.controller ?: service.disable()
    }


    fun checkTableExists() {
        var conn: Connection? = null
        try {
            conn = DriverManager.getConnection(config.url, config.user, config.pass)
            val metaData = conn.metaData

            // 注意：很多数据库驱动对表名大小写敏感，建议必要时转大写或小写
            val rs = metaData.getTables(null, params.schema, params.tables, arrayOf("TABLE"))
            val ne = rs.next()
           if (!ne){
               throw Exception("${params.tables}表不存在")
           }
        } catch (e: Exception) {
           throw e
        } finally {
            conn?.close()
        }
    }

}
