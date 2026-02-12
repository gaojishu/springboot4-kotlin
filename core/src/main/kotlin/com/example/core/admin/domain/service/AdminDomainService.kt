package com.example.core.admin.domain.service

import com.example.base.exception.BusinessException
import com.example.core.admin.domain.model.verifyIsDisabled
import com.example.core.admin.domain.model.verifyPassword
import com.example.data.admin.entity.AdminEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class AdminDomainService(
    private val passwordEncoder: BCryptPasswordEncoder,
) {

    /**
     * 单独校验用户名（如：修改用户名场景）
     */
    fun validateUsernameFormat(username: String) {
        val usernameRegex = """^[a-zA-Z0-9]{6,16}${'$'}""".toRegex()

        if (!username.matches(usernameRegex)) {
            throw BusinessException("用户名格式不正确：需6-16位字母或数字")
        }
    }

    /**
     * 单独校验密码（如：重置密码场景）
     */
    fun validatePasswordFormat(password: String) {
        val passwordRegex = """^(?=.*[A-Za-z])(?=.*\d)(?=.*[@${'$'}!%*?&._-])[A-Za-z\d@${'$'}!%*?&._-]{8,16}${'$'}""".toRegex()

        if (!password.matches(passwordRegex)) {
            throw BusinessException("密码强度不足：需8-16位且包含字母、数字及特殊符号@${'$'}!%*?&._-")
        }

    }

    /**
     * 聚合校验登录入参格式
     */
    fun validateUsernameAndPasswordFormat(username: String, password: String) {
        validateUsernameFormat(username)
        validatePasswordFormat(password)
    }

    /**
     * 验证登录模型（聚合校验）
     */
    fun verifyLogin(admin: AdminEntity,rawPassword: String) {

        //1. 密码校验
        admin.verifyPassword(rawPassword, passwordEncoder)

        // 2. 状态校验
        admin.verifyIsDisabled()
    }


}
