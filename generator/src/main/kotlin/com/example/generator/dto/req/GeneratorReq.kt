package com.example.generator.dto.req

data class GeneratorReq(
    val tables: String,
    val schema: String,

    val entity: String?,
    val entityPack: String,
    val entityPath: String,

    val mapper: String?,
    val mapperPack: String,
    val mapperPath: String,

    val mapperXml: String?,
    val mapperXmlPack: String,
    val mapperXmlPath: String,

    val service: String?,
    val servicePack: String,
    val servicePath: String,

    val serviceImpl: String?,
    val serviceImplPack: String,
    val serviceImplPath: String,

    val controller: String?,
    val controllerPack: String,
    val controllerPath: String
)
