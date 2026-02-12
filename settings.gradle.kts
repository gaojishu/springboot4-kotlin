rootProject.name = "spring"

// 注册所有模块
include( ":core",":data",":base",":api-admin")
// 👇 关键：在这里统一声明仓库
dependencyResolutionManagement {
    repositories {
        mavenCentral() // Spring Boot 和 Kotlin 官方库都在这里
        // 如果用到 Spring Milestone/快照版，才加下面这行：
        // maven("https://repo.spring.io/milestone")
    }
}