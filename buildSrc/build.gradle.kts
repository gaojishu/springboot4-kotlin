// buildSrc/build.gradle.kts
plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation(kotlin("allopen"))

    // 1. Flyway 插件及其驱动 (用于执行 flywayMigrate)
    implementation("org.flywaydb:flyway-gradle-plugin:11.20.3")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:11.20.3")

// Source: https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.7.10")

    // 2. jOOQ 插件及其代码生成核心
    implementation("org.jooq:jooq-codegen-gradle:3.19.30")
    implementation("org.jooq:jooq-codegen:3.19.30")
    implementation("org.jooq:jooq-meta:3.19.30")

}

