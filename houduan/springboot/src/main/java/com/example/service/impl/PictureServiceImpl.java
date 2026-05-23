package com.example.service.impl;

import cloud.liblibai.client.LibLib;
import cloud.liblibai.openapi.client.ApiException;
import cloud.liblibai.openapi.client.model.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.db.sql.Wrapper;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.enums.ResultCodeEnum;
import com.example.controller.FileController;
import com.example.entity.*;
import com.example.entity.pictureDto.*;
import com.example.exception.CustomException;
import com.example.mapper.PictureMapper;
import com.example.mapper.UserMapper;
import com.example.service.AiService;
import com.example.service.PictureService;
import com.example.service.UserService;
import com.example.utils.OssUtils;
import com.example.utils.TokenUtils;
import com.example.utils.translateUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
* @author 林泽楷
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-03-11 09:08:24
*/
@Service
public class PictureServiceImpl implements PictureService {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    private static final String filePath = System.getProperty("user.dir") + "/files/";

    @Value("${fileBaseUrl:}")
    private String fileBaseUrl;
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private PictureMapper pictureMapper;
    @Resource
    private AiService aiService;
    private Boolean isFirst=true;
    @Override
    public uploadPictureDto generate(MultipartFile file, String params, String modelName) throws IOException, InterruptedException, ApiException, URISyntaxException {
//        User currentUser = TokenUtils.getCurrentUser();
//        Integer id = currentUser.getId();
//        Picture picture = new Picture();
//        picture.setCreateTime(DateUtil.now());
        //文件存储
        StringBuilder oldPicture = OssUtils.uploadPicture(file, "oldPicture");
//        if (isFirst) {
//            picture.setOldPicture(oldPicture.toString());
//        }
//        isFirst=false;
        //-----params中翻译英
        String translatedParams = translateUtils.translate(params);
        //--------图片生成
        String s = generatePicture(oldPicture.toString(), translatedParams, modelName);
        //oss
        StringBuilder newPicture = OssUtils.uploadPicture(OssUtils.getMulFileByPath(s),"newPicture");
//        picture.setNewPicture(newPicture.toString());
//        picture.setUserId(id);
//        picture.setStyle(modelName);
//        String roomStyle = modelName.substring(modelName.indexOf("风格")+ 2);
//        picture.setRoomStyle(roomStyle);

//        QueryWrapper<Picture> quaryWrapper = new QueryWrapper();
//        quaryWrapper.eq("new_picture",newPicture.toString());
//        Picture picture1 = pictureMapper.selectOne(quaryWrapper);
        uploadPictureDto uploadPictureDto = new uploadPictureDto();
        uploadPictureDto.setNewUrl(newPicture.toString());
        uploadPictureDto.setOldUrl(oldPicture.toString());
//        if(Objects.equals(stop, "true")){
//            int insert = pictureMapper.insert(picture);
//            if(insert != 1){
//                throw new CustomException(ResultCodeEnum.INSERT_ERROR);
//            }
//            isFirst=false;
//            return uploadPictureDto;
//        }
        return uploadPictureDto;
    }

    @Override
    public uploadPictureDto partGenerate(String pictureFile, MultipartFile drawedPictureFile, String params, String modelName) throws IOException, InterruptedException, ApiException, URISyntaxException {
        //文件存储
//        StringBuilder pictureFile1 = OssUtils.uploadPicture(pictureFile, "blackwhite");
        StringBuilder drawedPictureFile1 = OssUtils.uploadPicture(drawedPictureFile, "blackwhite");
        //-----params中翻译英
        String translatedParams = translateUtils.translate(params);
        //--------图片生成
        String s1 = generateBlackAndWhite(drawedPictureFile1.toString());
        System.out.println("s1="+s1);
        String s = generatePartPicture(pictureFile,s1, translatedParams, modelName);
        //oss
        StringBuilder newPicture = OssUtils.uploadPicture(OssUtils.getMulFileByPath(s),"newPicture");

        uploadPictureDto uploadPictureDto = new uploadPictureDto();
        uploadPictureDto.setNewUrl(newPicture.toString());
        uploadPictureDto.setOldUrl(pictureFile);
        return uploadPictureDto;
    }

    //
//    @Override
//    public uploadPictureDto add(MultipartFile file,String params,String modelName) throws InterruptedException, ApiException, IOException, URISyntaxException {
//        User currentUser = TokenUtils.getCurrentUser();
//
//        Integer id = currentUser.getId();
//        Picture picture = new Picture();
//        picture.setCreateTime(DateUtil.now());
//        //文件存储
//        StringBuilder oldPicture = OssUtils.uploadPicture(file, "oldPicture");
//        picture.setOldPicture(oldPicture.toString());
//        //-----params中翻译英
//        String translatedParams = translateUtils.translate(params);
//        //--------图片生成
//        String s = generatePicture(oldPicture.toString(), translatedParams, modelName);
//        //oss
//        StringBuilder newPicture = OssUtils.uploadPicture(OssUtils.getMulFileByPath(s),"newPicture");
//        picture.setNewPicture(newPicture.toString());
//        picture.setUserId(id);
//        picture.setStyle(modelName);
//        String roomStyle = modelName.substring(modelName.indexOf("风格")+ 2);
//        picture.setRoomStyle(roomStyle);
//
//        int insert = pictureMapper.insert(picture);
//        QueryWrapper<Picture> quaryWrapper = new QueryWrapper();
//        quaryWrapper.eq("new_picture",newPicture.toString());
//        Picture picture1 = pictureMapper.selectOne(quaryWrapper);
//        uploadPictureDto uploadPictureDto = new uploadPictureDto();
//        uploadPictureDto.setPictureId(picture1.getId());
//        uploadPictureDto.setNewUrl(newPicture.toString());
//        if(insert != 1){
//            throw new CustomException(ResultCodeEnum.INSERT_ERROR);
//        }
//        return uploadPictureDto;
//    }
@Override
public uploadPictureDto add(String oldPicture,String newPicture, String modelName) throws InterruptedException, ApiException, IOException, URISyntaxException {
        User currentUser = TokenUtils.getCurrentUser();

        Integer id = currentUser.getId();
        Picture picture = new Picture();
        picture.setCreateTime(DateUtil.now());
        picture.setOldPicture(oldPicture);
            picture.setNewPicture(newPicture);
        picture.setUserId(id);
        picture.setStyle(modelName);
        String roomStyle = modelName.substring(modelName.indexOf("风格")+ 2);
        picture.setRoomStyle(roomStyle);
          int insert = pictureMapper.insert(picture);
            if(insert != 1){
            throw new CustomException(ResultCodeEnum.INSERT_ERROR);
        }
        QueryWrapper<Picture> quaryWrapper = new QueryWrapper();
        quaryWrapper.eq("new_picture",newPicture.toString());
        Picture picture1 = pictureMapper.selectOne(quaryWrapper);
    uploadPictureDto uploadPictureDto = new uploadPictureDto();
    uploadPictureDto.setPictureId(picture1.getId());
    return uploadPictureDto;
}
    public String generatePicture(String url,String params,String modelName) throws ApiException, InterruptedException {
    LibLib api = new LibLib("p5D4QTZaCFuYtuA9K9SdwQ", "y4U17AKVFI5GdPmGS1KsOzUPOUPCikDt") ;// 填入你自己的key
    ImageToImageRequest imageToImageRequest = new ImageToImageRequest();
        ImageToImageRequestGenerateParams imageToImageRequestGenerateParams = chooseModel(params, url, modelName);
        imageToImageRequest.generateParams(imageToImageRequestGenerateParams);
    imageToImageRequest.templateUuid("63b72710c9574457ba303d9d9b8df8bd");
    //NOTE(gz): 异步 SDK 调用方法
    SubmitResponse submitResponse = api.submitImageToImage(imageToImageRequest);
    System.out.println(submitResponse);

    while(true) {
        StatusResponse status = api.getStatus(new StatusRequest().generateUuid(submitResponse.getData().getGenerateUuid()));
        System.out.println(status);
        if (status.getData().getGenerateStatus() == GenerateStatus.SUCCEED) {
            System.out.println(status.getData().getImages());
            List<Image> images = status.getData().getImages();
            String imageUrl = images.get(0).getImageUrl();
            return imageUrl;
        }
        Thread.sleep(5000);
    }
}
    public String generatePartPicture(String pictureUrl,String picturePartUrl,String params,String modelName) throws ApiException, InterruptedException {
        LibLib api = new LibLib("p5D4QTZaCFuYtuA9K9SdwQ", "y4U17AKVFI5GdPmGS1KsOzUPOUPCikDt") ;// 填入你自己的key
        ImageToImageRequest imageToImageRequest = new ImageToImageRequest();
        ImageToImageRequestGenerateParams imageToImageRequestGenerateParams = choosePartModel(params, pictureUrl,picturePartUrl, modelName);
        imageToImageRequest.generateParams(imageToImageRequestGenerateParams);
        imageToImageRequest.templateUuid("63b72710c9574457ba303d9d9b8df8bd");
        //NOTE(gz): 异步 SDK 调用方法
        SubmitResponse submitResponse = api.submitImageToImage(imageToImageRequest);
        System.out.println(submitResponse);

        while(true) {
            StatusResponse status = api.getStatus(new StatusRequest().generateUuid(submitResponse.getData().getGenerateUuid()));
            System.out.println(status);
            if (status.getData().getGenerateStatus() == GenerateStatus.SUCCEED) {
                System.out.println(status.getData().getImages());
                List<Image> images = status.getData().getImages();
                String imageUrl = images.get(0).getImageUrl();
                return imageUrl;
            }
            Thread.sleep(5000);
        }
    }
    public String generateBlackAndWhite(String url) throws  InterruptedException, IOException {
        final ProcessBuilder processBuilder = new ProcessBuilder("python3", "/www/wwwroot/lzk.py",url);
        processBuilder.redirectErrorStream(true);
        final Process process = processBuilder.start();
        final BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String s = null;
        String res="";
        while ((s = in.readLine()) != null) {
            System.out.println("s="+s);
            res=s;
        }
        System.out.println("res="+res);
        final int exitCode = process.waitFor();
        System.out.println(exitCode == 0);
            return res;
    }
    public ImageToImageRequestGenerateParams chooseModel(String params,String url,String modelName){
        System.out.println(url);
        ImageToImageRequestGenerateParams imageToImageRequestGenerateParams = new ImageToImageRequestGenerateParams();
        //
        if(modelName.equals("奶油风格客厅")){
            //设置lora
                AdditionalNetwork additionalNetwork1 = new AdditionalNetwork();
                additionalNetwork1.setModelId("d74297829bbb4f0bb30228f28b5bf88d");
                additionalNetwork1.setWeight(0.5F);
                ArrayList<AdditionalNetwork> additionalNetwork = new ArrayList<>();
                additionalNetwork.add(additionalNetwork1);
                //
            //设置参数
                imageToImageRequestGenerateParams.prompt(params+"balcony,mural paintings,wall hangings,no human. no main lighting,use downlight to hide on roof,Ambient lighting blends soft afternoon sky light filtered through gauze curtains with 3000K warm artificial light sources. the balcony presents a naturally softened urban landscape (soft focus),filter.,")
                        .checkPointId("fa5e552314b59c4e4e0117e6f0b2d3b5").clipSkip(1).sampler(10).cfgScale(7).resizeMode(2).steps(30).seed(923320146)
                        .imgCount(1).resizedWidth(1440).resizedHeight(1080).denoisingStrength(0.75F).resizeMode(0).mode(0)
                        .additionalNetwork(additionalNetwork)
                        .sourceImage(url);
            }
        else if(modelName.equals("极简风格客厅")){
            //设置lora
            AdditionalNetwork additionalNetwork1 = new AdditionalNetwork();
            additionalNetwork1.setModelId("7b78bb59cd1043f89eaa57c676951ba7");
            additionalNetwork1.setWeight(0.7F);
            ArrayList<AdditionalNetwork> additionalNetwork = new ArrayList<>();
            additionalNetwork.add(additionalNetwork1);
            //设置参数
            imageToImageRequestGenerateParams.prompt(params+"Minimalism,Minimalism,Minimalism,living room,")        .checkPointId("412b427ddb674b4dbab9e5abd5ae6057").clipSkip(3).sampler(10).cfgScale(7).resizeMode(2).steps(30).seed(92215291)
                    .imgCount(1).resizedWidth(1440).resizedHeight(1080).denoisingStrength(0.75F).resizeMode(2).mode(0)
                    .additionalNetwork(additionalNetwork)
                    .sourceImage(url);
        }
        else if(modelName.equals("极简风格卧室")){
            //设置lora
            AdditionalNetwork additionalNetwork1 = new AdditionalNetwork();
            additionalNetwork1.setModelId("dd313a0cf46242f08bc0adbc51097098");
            additionalNetwork1.setWeight(0.7F);
            ArrayList<AdditionalNetwork> additionalNetwork = new ArrayList<>();
            additionalNetwork.add(additionalNetwork1);
            //设置参数
            imageToImageRequestGenerateParams.prompt(params+"no humans,pillow,bed,plant,window,indoors,sunlight,wooden floor,curtains,shadow,lamp,bedroom,day,potted plant,").checkPointId("fa5e552314b59c4e4e0117e6f0b2d3b5").clipSkip(2).sampler(15).cfgScale(7).resizeMode(2).steps(30).seed(92215291)
                    .imgCount(1).resizedWidth(1440).resizedHeight(1080).denoisingStrength(0.80F).resizeMode(0).mode(0)
                    .additionalNetwork(additionalNetwork)
                    .sourceImage(url);
        }
        else if(modelName.equals("极简风格浴室")){
            //设置lora
            AdditionalNetwork additionalNetwork1 = new AdditionalNetwork();
            additionalNetwork1.setModelId("ee3ce478dfde4b5ba0b1130a57ac0427");
            additionalNetwork1.setWeight(0.7F);
            ArrayList<AdditionalNetwork> additionalNetwork = new ArrayList<>();
            additionalNetwork.add(additionalNetwork1);
            //设置参数
            imageToImageRequestGenerateParams.prompt(params+"Lao chen,The sanitation area places the washbasin and toilet against the wall. The washing area is equipped with a shelf for toiletries. The shower should be set in the corner of the wall. The shower can use a linear shower panel or a simple hand shower.  The color scheme is unified as white or light colors,with pipes concealed.")
                    .checkPointId("412b427ddb674b4dbab9e5abd5ae6057").clipSkip(2).sampler(7).cfgScale(6).steps(25).seed(-1)
                    .imgCount(1).resizedWidth(1440).resizedHeight(1080).denoisingStrength(0.75F).resizeMode(0).mode(0)
                    .additionalNetwork(additionalNetwork)
                    .sourceImage(url);
        }
        else if(modelName.equals("浅色风格浴室")){
            //设置ControlNet
            ArrayList<ControlNet> aControlNet = new ArrayList<>();
            //设置ControlNet1
            System.out.println("设置ControlNet1");
            ControlNet controlNet = new ControlNet();
            //设置annotationParameters
            Map<String, Object> annotationParameters1 = new HashMap<>();
            annotationParameters o = new annotationParameters();
            annotationParameters1.put("invert", o);
            //-------------
            controlNet.unitOrder(1).model("7168cece6a0d491375aa1753ff3bdc21").width(1024).height(768).preprocessor(35).controlMode(1).resizeMode(1).startingControlStep(0).endingControlStep(1).pixelPerfect(1).annotationParameters(annotationParameters1).controlWeight(0.6F).setSourceImage(url);
            aControlNet.add(controlNet);
            //设置lora
            AdditionalNetwork additionalNetwork1 = new AdditionalNetwork();
            additionalNetwork1.setModelId("6b350a19341c4d81a149c67378bad66e");
            additionalNetwork1.setWeight(0.7F);
            ArrayList<AdditionalNetwork> additionalNetwork = new ArrayList<>();
            additionalNetwork.add(additionalNetwork1);
            //------参数传递

            System.out.println("参数传递");
            imageToImageRequestGenerateParams.prompt(params+"TOILET,shower room,bathtub,modern minimalism,sunlight exposure,Cool color tone,warm,sunshine,oversized floor-to-ceiling windows,absurdres,incredibly absurdres,reality,realistic,Cinematic Lighting,moody lighting,available light,Tyndall effect,((art nouveau)),")
                    .checkPointId("0444f77f29bc4e4890df87f62d1fffc6").clipSkip(2).sampler(10).cfgScale(7).resizeMode(2).steps(30).imgCount(1).resizedWidth(1024).resizedHeight(1024).denoisingStrength(0.75F).seed(1514589581)
                    .sourceImage(url)
                    .additionalNetwork(additionalNetwork)
            ;
        }
        else if(modelName.equals("原木风格餐厅")){
            //设置lora
            AdditionalNetwork additionalNetwork1 = new AdditionalNetwork();
            additionalNetwork1.setModelId("b057fac79aba4c72ad1234c17c55c304");
            additionalNetwork1.setWeight(0.8F);
            ArrayList<AdditionalNetwork> additionalNetwork = new ArrayList<>();
            additionalNetwork.add(additionalNetwork1);
            //------参数传递
            System.out.println("参数传递");
            imageToImageRequestGenerateParams.prompt(params)
                    .checkPointId("52f5c79bb2f6f5e1631828e33dd1928c").clipSkip(2).sampler(20).cfgScale(7).resizeMode(2).steps(32).imgCount(1).resizedWidth(1440).resizedHeight(1080).denoisingStrength(0.67F).seed(1008728757).resizeMode(2).mode(0)
                    .sourceImage(url)
                    .additionalNetwork(additionalNetwork)
            ;
        }

        System.out.println(imageToImageRequestGenerateParams);
        return imageToImageRequestGenerateParams;
    }

    public ImageToImageRequestGenerateParams choosePartModel(String params,String pictureUrl,String picturePartUrl,String modelName){
        System.out.println(pictureUrl);
        ImageToImageRequestGenerateParams imageToImageRequestGenerateParams = new ImageToImageRequestGenerateParams();
        //局部绘图参数
        InpaintParams inpaintParams = new InpaintParams();
        inpaintParams.setMaskImage(picturePartUrl);
        inpaintParams.setMaskMode(1);
        inpaintParams.setMaskPadding(32);
        inpaintParams.setMaskBlur(4);
        inpaintParams.setInpaintArea(0);
        inpaintParams.setInpaintingFill(1);

//        if(modelName.equals("奶油风格客厅")){
            imageToImageRequestGenerateParams.prompt(params)
                    .checkPointId("fa5e552314b59c4e4e0117e6f0b2d3b5").clipSkip(3).sampler(10).cfgScale(7).resizeMode(2).steps(30).seed(923320146)
                    .imgCount(1).resizedWidth(1440).resizedHeight(1080).denoisingStrength(0.95F).resizeMode(0).mode(4)
                    .inpaintParam(inpaintParams)
                    .sourceImage(pictureUrl);
//        }
//        else if(modelName.equals("极简风格客厅")){
//            imageToImageRequestGenerateParams.prompt(params)
//                    .checkPointId("1ab9c57ba12a492fb46c5497fdb294e5").clipSkip(3).sampler(10).cfgScale(7).resizeMode(2).steps(30).seed(92215291)
//                    .imgCount(1).resizedWidth(1440).resizedHeight(1080).denoisingStrength(0.95F).resizeMode(2).mode(4)
//                    .inpaintParam(inpaintParams)
//                    .sourceImage(pictureUrl);
//        }
//        else if(modelName.equals("极简风格卧室")){
//            imageToImageRequestGenerateParams.prompt(params)
//                    .checkPointId("3ebf7838efef4bca8276c2cad4a4e33d").clipSkip(3).sampler(15).cfgScale(7).resizeMode(2).steps(30).seed(92215291)
//                    .imgCount(1).resizedWidth(1440).resizedHeight(1080).denoisingStrength(0.95F).resizeMode(0).mode(4)
//                    .inpaintParam(inpaintParams)
//                    .sourceImage(pictureUrl);
//        }
//        else if(modelName.equals("极简风格浴室")){
//
//            //设置参数
//            imageToImageRequestGenerateParams.prompt(params)
//                    .checkPointId("412b427ddb674b4dbab9e5abd5ae6057").clipSkip(4).sampler(20).cfgScale(7).steps(30).seed(923320146)
//                    .imgCount(1).resizedWidth(1440).resizedHeight(1080).denoisingStrength(0.95F).resizeMode(2)
//                    .mode(4)
//                    .inpaintParam(inpaintParams)
//                    .sourceImage(pictureUrl);
//        }
//        else if(modelName.equals("浅色风格浴室")){
//
//
//            System.out.println("参数传递");
//            imageToImageRequestGenerateParams.prompt(params)
//                    .checkPointId("412b427ddb674b4dbab9e5abd5ae6057").clipSkip(2).sampler(10).cfgScale(7).resizeMode(2).steps(30).imgCount(1).resizedWidth(1024).resizedHeight(1024).denoisingStrength(0.95F).seed(1514589581)
//                    .mode(4)
//                    .inpaintParam(inpaintParams)
//                    .sourceImage(pictureUrl)
//
//            ;
//        }
//        else if(modelName.equals("原木风格餐厅")){
//
//            System.out.println("参数传递");
//            imageToImageRequestGenerateParams.prompt(params)
//                    .checkPointId("52f5c79bb2f6f5e1631828e33dd1928c").clipSkip(3).sampler(20).cfgScale(7).resizeMode(2).steps(32).imgCount(1).resizedWidth(1024).resizedHeight(1024).denoisingStrength(0.95F).seed(1008728757)
//                    .mode(4)
//                    .inpaintParam(inpaintParams)
//                    .sourceImage(pictureUrl)
//            ;
//        }

        System.out.println(imageToImageRequestGenerateParams);
        return imageToImageRequestGenerateParams;
    }



    @Override
    public void updateById(Picture picture) {
        int i = pictureMapper.updateById(picture);
        if(i != 1){
            throw new CustomException(ResultCodeEnum.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteById(Integer id) {
        pictureMapper.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        pictureMapper.deleteByIds(ids);
    }

    @Override
    public Picture selectById(Integer id) {
        Picture picture = pictureMapper.selectById(id);
        return picture;
    }
    @Override
    public IPage<QuaryPictureDto> selectPage(QuaryPictureDto quaryPictureDto, Integer pageNum, Integer pageSize) {
        IPage<QuaryPictureDto> page = new Page<>(pageNum, pageSize);
        //queryWrapper组装查s询where条件
        MPJLambdaWrapper<Picture> mpjLambdaWrapper = JoinWrappers.lambda(Picture.class);
        mpjLambdaWrapper.selectAll(Picture.class)
                .select(User::getUserName)  // 查询 Role 表的 name 字段
                .leftJoin(User.class,User::getId,Picture::getUserId) // 关联 user_role
                .eq(quaryPictureDto.getUserId()!=null, Picture::getUserId,quaryPictureDto.getUserId())
                .like(User::getUserName, quaryPictureDto.getUserName());
        //分页查询 （需要启用 mybatis plus 分页插件）
        IPage<QuaryPictureDto> listPage = pictureMapper.selectJoinPage(page, QuaryPictureDto.class, mpjLambdaWrapper);
        return listPage;
    }

    @Override
    public List<QuaryPictureTime> getTime() {
        //queryWrapper组装查s询where条件
        MPJLambdaWrapper<Picture> mpjLambdaWrapper = JoinWrappers.lambda(Picture.class);
        mpjLambdaWrapper.select(Picture::getCreateTime)
                .select(User::getUserIp)  // 查询 Role 表的 name 字段
                .leftJoin(User.class,User::getId,Picture::getUserId); // 关联 user_role
        //连表查询 返回自定义ResultType
        List<QuaryPictureTime> time = pictureMapper.selectJoinList(QuaryPictureTime.class, mpjLambdaWrapper);
        System.out.println(time);
        return time;
    }

    @Override
    public String getDecorationSuggestions(String url, String money) throws InterruptedException {
        String s = aiService.doImgToText("你是一个装修公司的装修设计师，请你理解这个刚装修好的房子图片，现在用" + money + "元的预算，装修这个房子然后给出这个房子的预算分析，并对每一个家具进行品牌的推荐。不要有开头语句和结论，500字左右", url, 0.95F);
        UpdateWrapper<Picture> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("new_picture",url);
        Picture picture = new Picture();
        picture.setBudget(Integer.valueOf(money));
        pictureMapper.update(picture,updateWrapper);
        return s;
    }

    @Override
    public List<QuaryPictureMoney> getMoney() {
        //queryWrapper组装查s询where条件
        MPJLambdaWrapper<Picture> mpjLambdaWrapper = JoinWrappers.lambda(Picture.class);
        mpjLambdaWrapper.select(Picture::getBudget)
                .select(User::getUserIp)  // 查询 Role 表的 name 字段
                .leftJoin(User.class,User::getId,Picture::getUserId); // 关联 user_role
        //连表查询 返回自定义ResultType
        List<QuaryPictureMoney> money = pictureMapper.selectJoinList(QuaryPictureMoney.class, mpjLambdaWrapper);
        System.out.println(money);
        return money;
    }

    @Override
    public List<QuaryPictureStyle> getStyles() {
        //queryWrapper组装查s询where条件
        MPJLambdaWrapper<Picture> mpjLambdaWrapper = JoinWrappers.lambda(Picture.class);
        mpjLambdaWrapper.select("style as name").select("COUNT(*) AS value").groupBy(Picture::getStyle);
        ;;
        //连表查询 返回自定义ResultType
        List<QuaryPictureStyle> PictureStyle = pictureMapper.selectJoinList(QuaryPictureStyle.class, mpjLambdaWrapper);
        System.out.println(PictureStyle);
        return PictureStyle;
    }



    @Override
    public List<QuaryPictureDto> selectAll(QuaryPictureDto quaryPictureDto) throws NoSuchMethodException {
        //queryWrapper组装查s询where条件
        MPJLambdaWrapper<Picture> mpjLambdaWrapper = JoinWrappers.lambda(Picture.class);
        mpjLambdaWrapper.selectAll(Picture.class)
                .select(User::getUserName)  // 查询 Role 表的 name 字段
                .leftJoin(User.class,User::getId,Picture::getUserId) // 关联 user_role
                .like(quaryPictureDto.getUserName()!=null,User::getUserName, quaryPictureDto.getUserName())
                .eq(quaryPictureDto.getUserId()!=null, Picture::getUserId,quaryPictureDto.getUserId());
        //连表查询 返回自定义ResultType
        List<QuaryPictureDto> pictures = pictureMapper.selectJoinList(QuaryPictureDto.class, mpjLambdaWrapper);
        return pictures;
    }

}




