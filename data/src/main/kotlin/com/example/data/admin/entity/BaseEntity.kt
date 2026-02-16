package com.example.data.admin.entity

import com.baomidou.mybatisplus.annotation.*
import java.time.LocalDateTime

abstract class BaseEntity {

    @TableId(type = IdType.AUTO)
    var id: Long? = null

    /** 创建时间：仅在插入时填充 */
    @TableField(fill = FieldFill.INSERT)
    var createdAt: LocalDateTime? = null

    /** 更新时间：在插入和更新时均填充 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    var updatedAt: LocalDateTime? = null

}
