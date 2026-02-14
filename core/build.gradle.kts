// core 模块：存放业务逻辑 (Service/Domain)


dependencies {
    // 引用内部模块
    implementation(project(":base"))

    implementation(project(":data"))


    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-validation")

    // Source: https://mvnrepository.com/artifact/cn.hutool/hutool-captcha
    implementation("cn.hutool:hutool-captcha:5.8.43")

    // Source: https://mvnrepository.com/artifact/org.mapstruct/mapstruct
    implementation("org.mapstruct:mapstruct:1.6.3")
    kapt("org.mapstruct:mapstruct-processor:1.6.3")
    // Source: https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-jsqlparser
    api("com.baomidou:mybatis-plus-jsqlparser:3.5.16")

}

// --- 打包配置：防止生成无法引用的可执行 Jar ---

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false // 核心模块不需要 main 方法，禁用 bootJar
}

tasks.named<Jar>("jar") {
    enabled = true  // 开启普通 Jar 打包，这样 api 模块才能 import 这里的类
    archiveClassifier.set("") // 确保生成的 jar 文件名不带 -plain 后缀
}
