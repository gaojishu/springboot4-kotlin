package com.example.generator.dto

data class GeneratorParamsDTO(
    val tables: String,
    val schema: String,
    val entity: Boolean,
    val entityPack: String,
    val mapper: Boolean,
    val mapperPack: String,
    val mapperPath: String,
    val service: Boolean,
    val servicePack: String,
    val servicePath: String,
    val controller: Boolean,
    val controllerPack: String,
    val controllerPath: String
)