package com.example.service;

import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ModelData;
import io.reactivex.Flowable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface SSEService {
   Flowable<ModelData> doStreamRequest(String url, String userMessage, Float temperature);
   Flowable<ModelData> doStreamRequest(List<ChatMessage> messages, Float temperature);
}
