package com.example.core.admin.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class LoginAdmin(
    val id: Long,
    username: String,
    password: String,
    disabled: Boolean = false,
    authorities: Collection<GrantedAuthority>
) : User(username, password, authorities)
