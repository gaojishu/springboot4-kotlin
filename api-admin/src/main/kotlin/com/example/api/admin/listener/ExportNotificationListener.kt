package com.example.api.admin.listener

import com.example.api.admin.constants.StompConstants
import com.example.api.admin.dto.res.StompMessageRes
import com.example.api.admin.event.ExportFinishedEvent
import com.example.core.admin.service.AdminService
import com.example.core.admin.service.NoticeService
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class ExportNotificationListener(
    private val messagingTemplate: SimpMessagingTemplate,
    private val noticeService: NoticeService,
    private val adminService: AdminService
) {

    @EventListener
    fun handleExportFinished(event: ExportFinishedEvent) {
        val admin = adminService.selectByUsername(event.username)
        noticeService.create(
            admin?.id,
            event.title,
            event.content,
            event.attachments
        )

        val message = StompMessageRes(
            title = event.title,
            content = event.content
        )

        messagingTemplate.convertAndSendToUser(event.username, StompConstants.QUEUE_NOTIFICATIONS, message)
    }
}
