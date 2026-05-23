package com.example.service;

import com.zhipu.oapi.service.v4.model.ChatMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface AiService {
   String doRequest(List<ChatMessage> messages, Boolean stream, Float temperature);
   String doRequest(String systemMessage, String userMessage, Boolean stream, Float temperature);
   String doSyncRequest(String systemMessage, String userMessage, Float temperature);
   String doImgToText(String userMessage, String url, Float temperature) throws InterruptedException;
}
