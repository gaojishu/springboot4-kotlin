plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("kapt")

    id("org.springframework.boot") version "4.0.3" apply false
    id("io.spring.dependency-management") version "1.1.7"
}
group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    maven {
        url = uri("https://maven.aliyun.com/repository/public/")
    }
    mavenLocal()
    mavenCentral()
}



// 针对所有子模块的通用配置
subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    dependencies {

        implementation("org.springframework.boot:spring-boot-starter")
        // 必须包含此依赖，Spring 才会自动配置 ObjectMapper Bean
        implementation("org.springframework.boot:spring-boot-starter-json")

        implementation("org.jetbrains.kotlin:kotlin-reflect")
        // Source: https://mvnrepository.com/artifact/tools.jackson.module/jackson-module-kotlin
        implementation("tools.jackson.module:jackson-module-kotlin:3.0.+")

        // Source: https://mvnrepository.com/artifact/tools.jackson.datatype/jackson-datatype-jsr310
        implementation("tools.jackson.datatype:jackson-datatype-jsr310:3.0.+")

// Source: https://mvnrepository.com/artifact/tools.jackson.core/jackson-databind
        implementation("tools.jackson.core:jackson-databind:3.0.+")




        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
        }
    }

    tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        enabled = false
    }

}
// 针对需要启动的模块单独开启
project(":api-admin") {
    tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        enabled = true
        // 手动指定 mainClass（如果自动识别失败）
        mainClass.set("com.example.api.admin.AdminApplicationKt")
    }
}