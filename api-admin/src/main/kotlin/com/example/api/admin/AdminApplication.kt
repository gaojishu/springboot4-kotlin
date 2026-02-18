package com.example.api.admin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [ "com.example.api.admin","com.example.core", "com.example.data", "com.example.base"]
)
class AdminApplication

fun main(args: Array<String>) {
    runApplication<AdminApplication>(*args)
}
