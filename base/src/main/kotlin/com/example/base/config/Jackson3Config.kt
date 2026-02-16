package com.example.base.config

import com.example.base.serializer.EmptyStringToNull
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tools.jackson.databind.module.SimpleModule

@Configuration
class Jackson3Config {

    @Bean
    fun objectMapper(): JsonMapperBuilderCustomizer {

        val module = SimpleModule()
        module.addDeserializer(String::class.java, EmptyStringToNull())
        val b = JsonMapperBuilderCustomizer{ builder->
            builder.addModules(module)
        }
        return b
    }
}
