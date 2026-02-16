// buildSrc/src/main/kotlin/jooq-conventions.gradle.kts
import org.jooq.meta.jaxb.Logging

plugins {
    id("org.jooq.jooq-codegen-gradle")
}

jooq {

    executions {
        create("main") {
            configuration {
                logging = Logging.WARN
                jdbc {
                    // 推荐从根目录的 gradle.properties 读取，避免写死
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
                        // 引用刚才抽取的配置
                        forcedTypes.addAll(JooqConfig.forcedTypes)
                    }
                    target {
                        packageName = "com.example.data.generated.admin"
                        directory = "build/generated-sources/jooq"
                    }
                }
            }
        }
    }
}
