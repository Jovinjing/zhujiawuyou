package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.service.AiService;
import com.example.service.UserService;
import com.example.service.impl.AdminService;
import com.example.service.impl.UserServiceImpl;
import com.example.utils.FileTransJavaDemo;
import com.example.utils.SpeechTranscriberDemo;
import com.example.utils.translateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@RestController
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;
    @Resource

    private RedisTemplate<String, Object> redisTemplate;
@Resource
private AiService aiService;
    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello ( ) throws InterruptedException, IOException {

        final String accessKeyId ="LTAI5t5tjo4s9Kr932FcEuzz";
        final String accessKeySecret = "eSWWAXg8EnwjvZk0qig3K7gq47Xoeq";
        final String appKey = "wsN6zODyWmdioZJK";
        String fileLink = "https://lzk-picture.oss-cn-guangzhou.aliyuncs.com/video/giOQdp9qJZmo1297486753ebdf53fe934add5f52c751.durationTime=2475.wav";
        FileTransJavaDemo demo = new FileTransJavaDemo(accessKeyId, accessKeySecret);
        // 第一步：提交录音文件识别请求，获取任务ID用于后续的识别结果轮询。
        String taskId = demo.submitFileTransRequest(appKey, fileLink);
        if (taskId != null) {
            System.out.println("录音文件识别请求成功，task_id: " + taskId);
        }
        else {
            System.out.println("录音文件识别请求失败！");
            return Result.success();
        }
        // 第二步：根据任务ID轮询识别结果。
        String result = demo.getFileTransResult(taskId);
        JSONObject jsonResult = JSONObject.parseObject(result);
        StringBuilder text= new StringBuilder();
        if (result != null) {
            System.out.println("录音文件识别结果查询成功：" + result);
            System.out.println(jsonResult.getJSONArray("Sentences"));
            for (int i = 0; i < jsonResult.getJSONArray("Sentences").size(); i++) {
                text.append(jsonResult.getJSONArray("Sentences").getJSONObject(i).getString("Text"));
            }
            return Result.success(text);
        }
        else {
            System.out.println("录音文件识别结果查询失败！");
            return Result.success();
        }

    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Admin account) {
        Account loginAccount = null;
            loginAccount = adminService.login(account);
        return Result.success(loginAccount);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register() {
        return Result.success();
    }

    /**
     * 修改密码
     */


}
