package com.example.generator.dto.req

data class GeneratorReq(
    val table: String,
    val schema: String,
    val method: List<String>?,

    val entity: Boolean?,
    val entityPack: String,
    val entityPath: String,

    val mapper: Boolean?,
    val mapperPack: String,
    val mapperPath: String,

    val mapperXml: Boolean?,
    val mapperXmlPack: String,
    val mapperXmlPath: String,

    val service: Boolean?,
    val servicePack: String,
    val servicePath: String,

    val serviceImpl: Boolean?,
    val serviceImplPack: String,
    val serviceImplPath: String,

    val controller: Boolean?,
    val controllerPack: String,
    val controllerPath: String
)
