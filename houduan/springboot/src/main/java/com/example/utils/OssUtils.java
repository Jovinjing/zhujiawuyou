package com.example.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
//import org.ini4j.CommonMultiMap;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OssUtils {
    public static StringBuilder uploadPicture(MultipartFile file,String newOrold) throws IOException {
        String fileName = file.getOriginalFilename();
        String accesskey="LTAI5tPotYspixKHy5DRnnxc";
        String accesskeysecret="RTUSdt68MlRRH9zqFQbuxP2wD1IqXR";
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-guangzhou.aliyuncs.com";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "lzk-picture";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName =newOrold+ "/"+fileName;
        // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。
        String region = "cn-guangzhou";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accesskey, accesskeysecret);
        // 创建PutObject请求。
        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(file.getBytes()));
//构建文件访问路径
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);
        System.out.println(stringBuilder);
        return stringBuilder;
    }
    //file 转换为 MultipartFile
    public static  MultipartFile getMulFileByPath(String filePath) throws IOException, URISyntaxException {
        URL url = new URL(filePath);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        byte[] content = inputStream.readAllBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                Paths.get(url.getPath()).getFileName().toString(), // 文件名
                Paths.get(url.getPath()).getFileName().toString(), // 原始文件名
                "multipart/form-data", // MIME类型
                content
        );
        return mockMultipartFile;
    }
}
