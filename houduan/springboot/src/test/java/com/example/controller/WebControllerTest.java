package com.example.controller;

import cloud.liblibai.client.LibLib;
import cloud.liblibai.openapi.client.ApiException;
import cloud.liblibai.openapi.client.model.*;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.exceptions.ClientException;
import com.example.service.AiService;
import com.example.service.PictureService;
import jakarta.annotation.Resource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class WebControllerTest {
    @Resource
    private AiService aiService;
    @Resource
    private PictureService PictureService;
    @Test
    public void hello() throws ApiException, InterruptedException, ClientException, FileNotFoundException {
        String accesskey="";
        String accesskeysecret="";
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-guangzhou.aliyuncs.com";
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "lzkai";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "oldPicture/1.jpeg";
        // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。
        String region = "cn-guangzhou";
        String filePath= "https://liblibai-online.liblib.cloud/img/.png";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accesskey, accesskeysecret);

            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName,new FileInputStream(filePath));
//构建文件访问路径
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);
        System.out.println(stringBuilder);


        // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传文件。
//        PutObjectResult result = ossClient.putObject(putObjectRequest);
////        PictureService.generatePicture();
//        LibLib api = new LibLib("1-x_n719POMdMc6ebbJ1NQ", "xyI1_H-XZzCvYczjGs6lWoytGR13QqtT") ;// 填入你自己的key
//        ImageToImageRequest imageToImageRequest = new ImageToImageRequest();
//        ImageToImageRequestGenerateParams params = new ImageToImageRequestGenerateParams();
//        params.prompt("cat").imgCount(1).resizedWidth(1024).resizedHeight(1024).denoisingStrength(0.75F).cfgScale(7)
//                .setSourceImage("https://fc-sd-a2eb516wa.oss-cn-hangzhou.aliyuncs.com/oldPicture/1.jpeg");
//        ;
//        imageToImageRequest.generateParams(params);
//        imageToImageRequest.templateUuid(");
//        //NOTE(gz): 异步 SDK 调用方法
//        SubmitResponse submitResponse = api.submitImageToImage(imageToImageRequest);
//        System.out.println(submitResponse);
//
//        while(true) {
//            StatusResponse status = api.getStatus(new StatusRequest().generateUuid(submitResponse.getData().getGenerateUuid()));
//            System.out.println(status);
//            if (status.getData().getGenerateStatus() == GenerateStatus.SUCCEED) {
//                System.out.println(status.getData().getImages());
//                break;
//            }
//            Thread.sleep(5000);
//        }

//        //NOTE(gz): 同步 SDK 调用方法
//        StatusResponseData statusResponseData = api.imageToImage(imageToImageRequest);
//        if (statusResponseData.getGenerateStatus() == GenerateStatus.SUCCEED) {
//            System.out.println(statusResponseData.getImages());
//        }

//        // API访问密钥
//        String secretKey = "U";
//
//        // 请求API接口的uri地址
//        String uri = "/api/generate/webui/status";
//        // 当前毫秒时间戳
//        Long timestamp = System.currentTimeMillis();
//        // 随机字符串
//        String signatureNonce = RandomStringUtils.randomAlphanumeric(10);
//        // 拼接请求数据
//        String content = uri + "&" + timestamp + "&" + signatureNonce;
//
//        try {
//            // 生成签名
//            SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");
//            Mac mac = Mac.getInstance("HmacSHA1");
//            mac.init(secret);
//            System.out.println(timestamp);
//            System.out.println(signatureNonce);
//            System.out.println(Base64.encodeBase64URLSafeString(mac.doFinal(content.getBytes())));
//
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("no such algorithm");
//        } catch (InvalidKeyException e) {
//            throw new RuntimeException(e);
//        }

    }
    private static String extractContent(String text, String startTag, String endTag) {
        int startIndex = text.indexOf(startTag) + startTag.length();
        int endIndex = text.indexOf(endTag, startIndex);
        return text.substring(startIndex, endIndex).trim();
    }

}