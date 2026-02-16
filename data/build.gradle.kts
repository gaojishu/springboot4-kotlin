plugins {
    `java-library` // 必须添加这个插件才能使用 api 关键字

   // id("nu.studer.jooq")

    id("org.jooq.jooq-codegen-gradle") version "3.19.30"


}
dependencies {
    // 引用内部模块
   implementation(project(":base"))

    jooqCodegen("org.postgresql:postgresql")

    //数据库
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    api("org.springframework.boot:spring-boot-starter-data-redis")

    // Source: https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-spring-boot4-starter
    api("com.baomidou:mybatis-plus-spring-boot4-starter:3.5.16")

    // Source: https://mvnrepository.com/artifact/com.github.yulichang/mybatis-plus-join-boot-starter
    api("com.github.yulichang:mybatis-plus-join-boot-starter:1.5.6")

    api("org.springframework.boot:spring-boot-starter-jooq")





}

// --- 打包配置：防止生成无法引用的可执行 Jar ---

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false // 核心模块不需要 main 方法，禁用 bootJar
}

tasks.named<Jar>("jar") {
    enabled = true  // 开启普通 Jar 打包，这样 api 模块才能 import 这里的类
    archiveClassifier.set("") // 确保生成的 jar 文件名不带 -plain 后缀
}

tasks.test {
    useJUnitPlatform()
}
jooq {

    executions {
        create("adminDb") {
            configuration {
                logging = org.jooq.meta.jaxb.Logging.WARN

                jdbc {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/spring"
                    user = "postgres"
                    password = "123321"
                }
                generator {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "admin"
                    }
                    target {
                        packageName = "com.example.data.generated"
                        directory = "build/generated-sources/jooq"
                    }
                }
            }
        }
    }
}
