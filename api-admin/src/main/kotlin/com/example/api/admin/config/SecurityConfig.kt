package com.example.api.admin.config

import com.example.api.admin.middleware.filter.SecurityFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import com.example.api.admin.handler.CustomAuthenticationEntryPoint
import com.example.api.admin.handler.CustomAccessDeniedHandler
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val customAccessDeniedHandler:CustomAccessDeniedHandler
) {
    @Bean
    fun filterChain(
        http: HttpSecurity,
        securityFilter: SecurityFilter
    ): SecurityFilterChain {
        http.addFilterBefore(securityFilter,UsernamePasswordAuthenticationFilter::class.java)

        val array = arrayOf(
            "/auth/login",
            "/auth/index",
            "/common/**",
            "/ws/**"
        )
        http.csrf { it.disable() }//禁用csrf
        http.cors {
            it.configurationSource(corsConfigurationSource())
        }
        http.httpBasic {
            it.disable()
        }
        http.authorizeHttpRequests {
            //允许所有用户,  受异常影响
            it.requestMatchers(*array).permitAll()
            //所有其他请求都需要经过认证
            it.anyRequest().authenticated()
        }
        http.formLogin { it.disable() }
        http.logout { it.disable() }
        http.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        http.exceptionHandling {
            it.authenticationEntryPoint(customAuthenticationEntryPoint)
            it.accessDeniedHandler(customAccessDeniedHandler)
        }
        return http.build()
    }

    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*") // 允许所有 Header
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
