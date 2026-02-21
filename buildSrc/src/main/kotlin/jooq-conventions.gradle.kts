// buildSrc/src/main/kotlin/jooq-conventions.gradle.kts
import org.jooq.meta.jaxb.Logging

plugins {
    id("org.jooq.jooq-codegen-gradle")
    id("org.flywaydb.flyway")
}
// 1. 抽取变量读取逻辑（带默认值防止报错）
val dbUrl = project.property("db.url").toString()
val dbUser = project.property("db.user").toString()
val dbPass = project.property("db.pass").toString()
val dbDriver = project.property("db.driver").toString()
val dbSchemas = project.property("db.schemas").toString().split(",").toTypedArray()


jooq {

    executions {
        create("main") {
            configuration {
                logging = Logging.WARN
                jdbc {
                    // 推荐从根目录的 gradle.properties 读取，避免写死
                    driver = dbDriver
                    url = dbUrl
                    user = dbUser
                    password = dbPass
                }
                generator {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "admin"
                        // 引用刚才抽取的配置
                        forcedTypes.addAll(JooqConfig.forcedTypes)
                    }
                    target {
                        packageName = "com.example.data.jooq"
                        directory = "build/generated-sources/jooq"
                    }
                }
            }
        }
    }
}

flyway {
    url = dbUrl
    user = dbUser
    password = dbPass
    schemas = dbSchemas
    locations = arrayOf("filesystem:src/main/resources/db/migration")
}

// 只有当 generateMainJooq 任务被插件创建后，才给它增加依赖
tasks.configureEach {
    if (this.name == "generateMainJooq") {
        this.dependsOn("flywayMigrate")
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
    this.dependsOn(tasks.matching { it.name == "generateMainJooq" })
}