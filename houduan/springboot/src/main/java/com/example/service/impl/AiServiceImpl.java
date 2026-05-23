package com.example.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.service.AiService;
import com.example.service.SSEService;
import com.example.utils.SSEUtils;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AiServiceImpl implements AiService {
    @Autowired
    private ClientV4 clientV4;
    @Resource
    private SSEService SSEService;
    @Override
    public  String doRequest(List<ChatMessage> messages, Boolean stream, Float temperature) {
        // 构造请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(stream)
                .invokeMethod(Constants.invokeMethod)
                .temperature(temperature)
                .messages(messages)
                .build();
        ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
        System.out.println(invokeModelApiResp);
        ChatMessage result = invokeModelApiResp.getData().getChoices().get(0).getMessage();
        return result.getContent().toString();
    }
    @Override
    public String doRequest(String systemMessage, String userMessage, Boolean stream, Float temperature) {
        // 构造请求
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        messages.add(systemChatMessage);
        messages.add(userChatMessage);
        return doRequest(messages, stream, temperature);
    }
    public String doSyncRequest(String systemMessage, String userMessage, Float temperature) {
        return doRequest(systemMessage, userMessage, Boolean.FALSE, temperature);
    }

    @Override
    public String doImgToText(String userMessage,String url, Float temperature) throws InterruptedException {



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
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4VPlus)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .temperature(temperature)
                .messages(messages)
                .build();
        ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
        System.out.println(invokeModelApiResp);
        return invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent().toString();
//流输出

//        // 建立 SSE 连接对象，0 表示不超时
//        SseEmitter emitter = new SseEmitter(0L);
//        // AI 生成，sse 流式返回
//        Flowable<ModelData> modelDataFlowable = SSEService.doStreamRequest(url, userMessage, 0.95F);
//        StringBuilder contentBuilder = new StringBuilder();
//        AtomicInteger flag = new AtomicInteger(0);
//        modelDataFlowable
//                // 异步线程池执行
//                .observeOn(Schedulers.io())
//                .map(chunk -> chunk.getChoices().get(0).getDelta().getContent())
//                .map(message -> message.replaceAll("\\s", ""))
//                .filter(StrUtil::isNotBlank)
//                .flatMap(message -> {
//                    // 将字符串转换为 List<Character>
//                    List<Character> charList = new ArrayList<>();
//                    for (char c : message.toCharArray()) {
//                        charList.add(c);
//                    }
//                    return Flowable.fromIterable(charList);
//                })
//                .doOnNext(c -> {
//                    {
//                        // 识别第一个 [ 表示开始 AI 传输 json 数据，打开 flag 开始拼接 json 数组
//                        if (c == '{') {
//                            flag.addAndGet(1);
//                        }
//                        if (flag.get() > 0) {
//                            contentBuilder.append(c);
//                        }
//                        if (c == '}') {
//                            flag.addAndGet(-1);
//                            if (flag.get() == 0) {
//                                // 累积单套题目满足 json 格式后，sse 推送至前端
//                                // sse 需要压缩成当行 json，sse 无法识别换行
//                                emitter.send(JSONUtil.toJsonStr(contentBuilder.toString()));
//                                // 清空 StringBuilder
//                                contentBuilder.setLength(0);
//                            }
//                        }
//                    }
//                }).doOnComplete(emitter::complete).subscribe();
//        return emitter;
    }

}
