package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.common.Result;
import com.example.entity.DataForm;
import com.example.service.DataFormService;
import com.example.utils.FileTransJavaDemo;
import com.example.utils.OssUtils;
import com.example.utils.SpeechTranscriberDemo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.nio.charset.StandardCharsets;
@RestController
@RequestMapping("/files")
public class FileController {
    @Resource
    private DataFormService DataFormService;

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    private static final String filePath = System.getProperty("user.dir") + "/files/";

    @Value("${fileBaseUrl:}")
    private String fileBaseUrl;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        //oss
        StringBuilder url = OssUtils.uploadPicture(file,"avatar");
//        String fileName = file.getOriginalFilename();
//        try {
//            if (!FileUtil.isDirectory(filePath)) {
//                FileUtil.mkdir(filePath);
//            }
//            fileName = System.currentTimeMillis() + "-" + fileName;
//            String realFilePath = filePath + fileName;
//            // 文件存储形式：时间戳-文件名
//            FileUtil.writeBytes(file.getBytes(), realFilePath);
//        } catch (Exception e) {
//            log.error(fileName + "--文件上传失败", e);
//        }
//        String url = fileBaseUrl + "/files/download/" + fileName;
        return Result.success(url);
    }

    /**
     * 获取文件
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        OutputStream os;
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath + fileName);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            log.warn("文件下载失败：" + fileName);
        }
    }



//文档生成

    @RequestMapping(value = "/downloadWord", method = RequestMethod.POST,produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity downloadWord(@RequestBody DataForm dataForm) {
        byte[] bytes = DataFormService.downloadWord(dataForm);
        String fileName = "downloadWord.docx";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity(bytes, headers, HttpStatus.OK);
    }
    @PostMapping( "/videoByWord")
    public Result videoByWord(MultipartFile file) throws IOException {
        StringBuilder videourl = OssUtils.uploadPicture(file,"video");

        final String accessKeyId ="";
        final String accessKeySecret = "q";
        final String appKey = "";
        String fileLink = videourl.toString();
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
}
