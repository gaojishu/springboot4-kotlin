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
}

// 禁用根项目的编译任务
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    enabled = false
}

// 禁用根项目的 Jar 打包
tasks.named<Jar>("jar") {
    enabled = false
}

// 如果有 Spring Boot 插件，禁用其 BootJar
tasks.findByPath("bootJar")?.enabled = false