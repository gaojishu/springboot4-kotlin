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

    }
}
