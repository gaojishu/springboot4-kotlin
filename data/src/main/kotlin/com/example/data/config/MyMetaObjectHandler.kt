package com.example.data.config

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import org.apache.ibatis.reflection.MetaObject
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class MyMetaObjectHandler : MetaObjectHandler {

    override fun insertFill(metaObject: MetaObject) {
        // 这里的 "createdAt" 必须和 BaseEntity 中的属性名完全一致
        this.strictInsertFill(metaObject, "createdAt", { LocalDateTime.now() }, LocalDateTime::class.java)
        this.strictInsertFill(metaObject, "updatedAt", { LocalDateTime.now() }, LocalDateTime::class.java)
    }

    override fun updateFill(metaObject: MetaObject) {
        // 更新时自动填入当前时间
        this.strictUpdateFill(metaObject, "updatedAt", { LocalDateTime.now() }, LocalDateTime::class.java)
    }
}