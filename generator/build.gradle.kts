
dependencies {

    //数据库
    runtimeOnly("org.postgresql:postgresql")

    implementation("com.baomidou:mybatis-plus-spring-boot4-starter:3.5.16")

    // 配置代码生成器
    // Source: https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-generator
    implementation("com.baomidou:mybatis-plus-generator:3.5.16")
    // Source: https://mvnrepository.com/artifact/org.apache.velocity/velocity-engine-core
    implementation("org.apache.velocity:velocity-engine-core:2.4.1")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-mustache") // 或 thymeleaf
}

