package com.example.api.admin.constants

object StompConstants {
    /**
     * 用户通知私有队列路径
     */
    const val QUEUE_NOTIFICATIONS = "/queue/notifications"

    /**
     * 公共广播路径
     */
    const val TOPIC_PUBLIC = "/topic/public"

    /**
     * 应用前缀（发送用）
     */
    const val APP_PREFIX = "/app"
}