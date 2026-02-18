import org.jooq.meta.jaxb.ForcedType

object JooqConfig {
    val forcedTypes = listOf(
        ForcedType().apply {
            userType = "com.example.data.admin.enums.files.FilesTypeEnum"
            converter = "com.example.data.admin.converter.files.FilesTypeConverter"
            includeExpression = "admin\\.files\\.type"
            includeTypes = "varchar"
        },
        ForcedType().apply {
            userType = "com.example.data.admin.enums.permission.PermissionTypeEnum"
            converter = "com.example.data.admin.converter.permission.PermissionTypeConverter"
            includeExpression = "admin\\.permission\\.type"
            includeTypes = "int2"
        },
        ForcedType().apply {
            userType = "com.example.data.admin.enums.admin.AdminDisabledStatusEnum"
            converter = "com.example.data.admin.converter.admin.AdminDisabledStatusConverter"
            includeExpression = "admin\\.admin\\.disabled_status"
            includeTypes = "int2"
        },
        ForcedType().apply {
            userType = "kotlin.collections.List<kotlin.String>"
            converter = "com.example.data.converter.JsonToListConverter"
            includeExpression = ".*\\.permission_key"
            includeTypes = "JSONB|JSON|TEXT"
        }

    )
}
