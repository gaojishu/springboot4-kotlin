package com.example.data

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@MapperScan("com.example.data") // 确保能扫描到你的 Mapper
class TestApplication
