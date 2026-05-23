package com.example.service;

import cloud.liblibai.openapi.client.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Login;
import com.example.entity.Picture;
import com.example.entity.User;
import com.example.entity.pictureDto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
* @author 林泽楷
* @description 针对表【user】的数据库操作Service
* @createDate 2025-03-11 09:08:24
*/
public interface PictureService {

// 添加图片
uploadPictureDto add(String oldPicture,String newPicture, String modelName) throws InterruptedException, ApiException, IOException, URISyntaxException;

    void updateById(Picture picture);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

    Picture selectById(Integer id);

    List<QuaryPictureDto> selectAll(QuaryPictureDto quaryPictureDto) throws NoSuchMethodException;
    IPage<QuaryPictureDto> selectPage(QuaryPictureDto quaryPictureDto, Integer pageNum, Integer pageSize);


    List<QuaryPictureTime> getTime();

    String getDecorationSuggestions(String url, String money) throws InterruptedException;

    List<QuaryPictureMoney> getMoney();

    List<QuaryPictureStyle> getStyles();

    uploadPictureDto generate(MultipartFile file, String params, String modelName) throws IOException, InterruptedException, ApiException, URISyntaxException;

    uploadPictureDto partGenerate(String pictureFile, MultipartFile drawedPictureFile, String params, String modelName) throws IOException, InterruptedException, ApiException, URISyntaxException;
}
