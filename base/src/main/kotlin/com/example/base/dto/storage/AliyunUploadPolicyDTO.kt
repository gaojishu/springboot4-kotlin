package com.example.base.dto.storage

data class AliyunUploadPolicyDTO(
    val accessId: String = "",
    val host: String = "",
    val policy: String = "",
    val signature: String = "",
    val expire: String = "",
    val callback: String? = null,
    val dir: String = ""
)