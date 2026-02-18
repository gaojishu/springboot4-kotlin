package com.example.base.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import tools.jackson.databind.ObjectMapper

@Configuration
class RedisConfig {
    @Bean
    fun redisTemplate(factory: RedisConnectionFactory,objectMapper: ObjectMapper): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = factory

        val jacksonSerializer = GenericJacksonJsonRedisSerializer(objectMapper)
        // Key 采用 String 序列化
        template.keySerializer = StringRedisSerializer()
        template.hashKeySerializer = StringRedisSerializer()
        // Value 采用 JSON 序列化 [Spring Data Redis Config](https://docs.spring.io)
        template.valueSerializer = jacksonSerializer
        template.hashValueSerializer = jacksonSerializer

        return template
    }
}
