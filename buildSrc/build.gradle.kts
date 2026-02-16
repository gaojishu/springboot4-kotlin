// buildSrc/build.gradle.kts
plugins {
    `kotlin-dsl` // 开启 Kotlin DSL 插件支持
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("tools.jackson.module:jackson-module-kotlin:3.0.4")

    // 必须包含这个依赖，JooqConfig.kt 才能识别 ForcedType 和相关配置类
    implementation("org.jooq:jooq-codegen:3.19.30")
    implementation("org.jooq:jooq-meta:3.19.30")

    // 如果你使用的是官方插件，也可以直接引入插件包
    implementation("org.jooq:jooq-codegen-gradle:3.19.30")
}
