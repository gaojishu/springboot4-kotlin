package com.example.api.admin.controller.websocket

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class ChatController {

    @Autowired private lateinit var template: SimpMessagingTemplate


    @MessageMapping("/hello")// 接收来自客户端的 /app/hello 消息
    @SendToUser("/queue/notifications") // 仅发回给发送者, 需要订阅 user/queue/notifications
    fun handleHello(principal: Principal, payload: String): String {
        return "你好 ${principal.name}, 服务器已收到消息$payload"
    }

    @MessageMapping("/touser")// 接收来自客户端的 /app/touser 消息
    fun handleMessage(principal: Principal, payload: String) {
        template.convertAndSendToUser(principal.name, "/queue/notifications", "服务器到消息$payload")
    }
}