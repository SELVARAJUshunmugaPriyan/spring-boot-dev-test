package com.simple.websocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.simple.websocket.model.ChatMessage;

@Controller
public class ChatController {

	@MessageMapping("/sendMessage")
	@SendTo("/topic/public")
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage)
	{
		return chatMessage;
	}
	
	@MessageMapping("/addUser")
	@SendTo("/topic/public")
	public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor)
	{
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		return chatMessage;
	}
	
	@MessageMapping("/removeUser")
	@SendTo("/topic/public")
	public ChatMessage removeUser(@Payload ChatMessage chatMessage)
	{
		return chatMessage;
	}
	
}
