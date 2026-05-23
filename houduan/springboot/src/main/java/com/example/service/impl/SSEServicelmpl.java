package com.example.service.impl;

import com.example.service.SSEService;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service

public class SSEServicelmpl implements SSEService {
    @Autowired
    private ClientV4 clientV4;
    public  Flowable<ModelData> doStreamRequest(String url, String userMessage, Float temperature) {
        // 构造请求
        List<ChatMessage> messages = new ArrayList<>();
        Object[] objects = new Object[2];
        HashMap<Object, Object> stringStringHashMap = new HashMap<>();
        HashMap<String, String> stringStringHashMap1 = new HashMap<>();
        stringStringHashMap1.put("url",url);
        stringStringHashMap.put("type","image_url");

        stringStringHashMap.put("image_url",stringStringHashMap1);
        HashMap<String, String> stringStringHashMap2 = new HashMap<>();
        stringStringHashMap2.put("text",userMessage);
        stringStringHashMap2.put("type","text");
        objects[0] = stringStringHashMap;
        objects[1] = stringStringHashMap2;
        ChatMessage imgChatMessage = new ChatMessage(ChatMessageRole.USER.value(), objects);
        System.out.println(imgChatMessage);
        messages.add(imgChatMessage);
        return doStreamRequest(messages, temperature);
    }

    /**
     * 通用流式请求
     *
     * @param messages
     * @param temperature
     * @return
     */
    public  Flowable<ModelData> doStreamRequest(List<ChatMessage> messages, Float temperature) {
        // 构造请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4V)
                .stream(Boolean.TRUE)
                .invokeMethod(Constants.invokeMethod)
                .temperature(temperature)
                .messages(messages)
                .build();
        ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
        return invokeModelApiResp.getFlowable();
    }
}
