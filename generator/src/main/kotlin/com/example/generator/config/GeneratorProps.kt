package com.example.generator.config

import java.io.File
import java.util.Properties

class GeneratorProps(rootDir: String) {
    val url: String
    val user: String
    val pass: String
    val dbSchema: String
    val dbPrefix: String

    val rootPath: String = rootDir
    val entityPack: String
    val mapperPack: String
    val mapperXmlPack: String
    val servicePack: String
    val serviceImplPack: String
    val controllerPack: String

    val entityPath: String
    val mapperPath: String
    val mapperXmlPath: String
    val servicePath: String
    val serviceImplPath: String
    val controllerPath: String

    init {
        val props = Properties().apply {
            val configFile = File("$rootDir/generator/src/main/resources/application.properties")
            if (!configFile.exists()) throw RuntimeException("配置文件缺失: ${configFile.absolutePath}")
            configFile.inputStream().use { load(it) }
        }
        url = props.getProperty("spring.datasource.url")
        user = props.getProperty("spring.datasource.username")
        pass = props.getProperty("spring.datasource.password")
        dbSchema = props.getProperty("db.schema")
        dbPrefix = props.getProperty("db.prefix")

        entityPack = props.getProperty("entity.pack")
        mapperPack = props.getProperty("mapper.pack")
        mapperXmlPack = props.getProperty("mapper.xml.pack")
        servicePack = props.getProperty("service.pack")
        serviceImplPack = props.getProperty("service.impl.pack")
        controllerPack = props.getProperty("controller.pack")

        entityPath = props.getProperty("entity.path")
        mapperPath = props.getProperty("mapper.path")
        mapperXmlPath = props.getProperty("mapper.xml.path")
        servicePath = props.getProperty("service.path")
        serviceImplPath = props.getProperty("service.impl.path")
        controllerPath = props.getProperty("controller.path")

    }
}
