package com.example.server.controller;

import com.example.server.pojo.Admin;
import com.example.server.pojo.common.ResponseBean;
import com.example.server.pojo.functional.ChatMsg;
import com.example.server.service.AdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final AdminService adminService;
    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate
            ,AdminService adminService){
        this.messagingTemplate=messagingTemplate;
        this.adminService=adminService;
    }

//    聊天接口,我要发消息
    @MessageMapping("/ws/chat")
    public void handleMsg(Authentication authentication, ChatMsg chatMsg){
        Admin admin=(Admin) authentication.getPrincipal();
        chatMsg.setFrom(admin.getUsername());
        chatMsg.setFromNickname(admin.getName());
        chatMsg.setDateTime(LocalDateTime.now());

//        发送消息
        messagingTemplate.convertAndSendToUser(chatMsg.getTo()
                ,"/queue/chat"
                ,chatMsg);

    }

    @ApiOperation(value = "获取所有的操作员")
    @GetMapping("/chat/admin")
    public ResponseBean getAllAdmins(@RequestParam(name = "keywords")String keywords){
        return ResponseBean.success("查询成功",adminService.getAdminsByKeyword(keywords));
    }


}
