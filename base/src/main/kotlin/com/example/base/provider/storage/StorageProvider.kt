package com.example.base.provider.storage

import com.example.base.dto.storage.AliyunUploadPolicyDTO
import java.io.File

interface StorageProvider {
    fun upload(file: File): String
    fun delete(path: String)
    fun exists(path: String): Boolean
    fun generatePresignedUrl(path: String): String
    fun move(from: String, to: String)
    fun copy(from: String, to: String)
    fun ossUploadPolicy(): AliyunUploadPolicyDTO
    fun rename(objectName: String, fileType: String): String
}