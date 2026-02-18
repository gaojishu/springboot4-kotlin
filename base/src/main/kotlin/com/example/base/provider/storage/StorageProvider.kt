package com.example.base.provider.storage

import com.example.base.dto.storage.AliyunUploadPolicyDTO

interface StorageProvider {
    fun upload(file: ByteArray, path: String): String
    fun delete(path: String)
    fun exists(path: String): Boolean
    fun generatePresignedUrl(path: String): String
    fun move(from: String, to: String)
    fun copy(from: String, to: String)
    fun ossUploadPolicy(): AliyunUploadPolicyDTO
    fun rename(objectName: String, fileType: String): String
}