plugins {
    kotlin("jvm")
}
// 统一配置 mainClass
group = "com.example.api.admin"
version = "0.0.1-SNAPSHOT"


dependencies {
    // 1. 引用核心业务逻辑模块
    implementation(project(":core"))
    implementation(project(":base"))
    implementation(project(":data"))



    // 2. 引入 Web 运行环境
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-websocket")


    // Kotlin 依赖在根目录 subprojects 已统一配置，此处无需重复
}


tasks.test {
    useJUnitPlatform()
}
