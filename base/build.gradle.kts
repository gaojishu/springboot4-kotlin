// 基础设施


dependencies {


    api("org.springframework.boot:spring-boot-starter-data-redis")

    // Source: https://mvnrepository.com/artifact/cn.hutool/hutool-captcha
    implementation("cn.hutool:hutool-captcha:5.8.43")

    // Source: https://mvnrepository.com/artifact/com.aliyun.oss/aliyun-sdk-oss
    implementation("com.aliyun.oss:aliyun-sdk-oss:3.18.5")


}

// --- 打包配置：防止生成无法引用的可执行 Jar ---
tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false // 核心模块不需要 main 方法，禁用 bootJar
}

tasks.named<Jar>("jar") {
    enabled = true  // 开启普通 Jar 打包，这样 api 模块才能 import 这里的类
    archiveClassifier.set("") // 确保生成的 jar 文件名不带 -plain 后缀
}
