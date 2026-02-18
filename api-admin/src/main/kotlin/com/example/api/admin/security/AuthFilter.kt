package com.example.api.admin.security

import com.example.base.provider.token.TokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthFilter(
    @Autowired private val userDetailsService: UserDetailsService,
    @Autowired private val tokenProvider: TokenProvider,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        // 1. 从 Header 提取
        var token = request.getHeader("Authorization")?.removePrefix("Bearer ")?.trim()

        // 2. 如果 Header 没有，尝试从 URL 参数 "token" 中获取
        if (token.isNullOrBlank()) {
            token = request.getParameter("token")?.trim()
        }

        if (!token.isNullOrEmpty()){

            try {
                val adminId = tokenProvider.getIdByToken(token)

                val userDetails: UserDetails = userDetailsService.loadUserByUsername(adminId.toString())

                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails, token, userDetails.authorities
                )
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            } catch (_: Exception) {
                SecurityContextHolder.clearContext()
            }
        }

        chain.doFilter(request, response)
    }
}