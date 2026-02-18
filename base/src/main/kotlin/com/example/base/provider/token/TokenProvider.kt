package com.example.base.provider.token

import com.example.base.dto.TokenDTO

interface TokenProvider {
    fun createToken(adminId: Long): TokenDTO
    fun getIdByToken(token: String): Long?
    fun deleteToken(token: String): Boolean
}