package com.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class translateUtils {
    public static String translate(String text) throws JsonProcessingException {
        String q = text;
        String from = "zh";
        String to = "en";
        String appid = ""; // 请替换为你的真实appid
        String salt = String.valueOf(System.currentTimeMillis()); // 随机盐值
        String key = ""; // 平台分配的密钥
        // 生成签名
        String sign = generateSign(appid, q, salt, key);

        // 构造请求URL
        String url = String.format("https://fanyi-api.baidu.com/api/trans/vip/translate?" +
                        "q=%s&from=%s&to=%s&appid=%s&salt=%s&sign=%s",
                q, from, to, appid, salt, sign);

        // 发送HTTP GET请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getBody());

        // 获取 trans_result 数组的第一个元素
        JsonNode transResult = rootNode.get("trans_result").get(0);
        String dst = transResult.get("dst").asText();
        return dst;
    }
    private static String generateSign(String appid, String q, String salt, String key) {
        String rawString = appid + q + salt + key;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(rawString.getBytes());

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("生成签名时发生错误", e);
        }
    }
}
