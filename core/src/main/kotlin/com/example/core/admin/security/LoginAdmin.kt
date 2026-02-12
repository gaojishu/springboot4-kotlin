package com.example.core.admin.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class LoginAdmin(
    val adminId: Long,
    val permissions: List<String>?,
    username: String,
    password: String,
    authorities: Collection<GrantedAuthority>
) : User(username, password, authorities)
