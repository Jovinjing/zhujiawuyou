package com.example.controller;

import cloud.liblibai.openapi.client.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.entity.Login;
import com.example.entity.Picture;
import com.example.entity.User;
import com.example.entity.pictureDto.*;
import com.example.service.PictureService;
import com.example.service.UserService;
import com.example.utils.TokenUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/picture")
public class PictureController {

    @Resource
    private PictureService pictureService;
    @Autowired
    private UserService userService;

    @GetMapping("/getStyles")
    public Result getStyles() throws InterruptedException, ApiException, IOException, URISyntaxException {
        List<QuaryPictureStyle> data=pictureService.getStyles();
        return Result.success(data);
    }

    @GetMapping("/getDecorationSuggestions")
    public Result getDecorationSuggestions(String url,String money) throws InterruptedException, ApiException, IOException, URISyntaxException {
        String data=pictureService.getDecorationSuggestions(url,money);
        return Result.success(data);
    }
    /**
     * 新增
     */
    @PostMapping("/generate")
    public Result generate(MultipartFile file,String params,String modelName) throws InterruptedException, ApiException, IOException, URISyntaxException {
        uploadPictureDto add = pictureService.generate(file, params, modelName);
        return Result.success(add);
    }
    @PostMapping("/partGenerate")
    public Result partGenerate(String pictureFile,MultipartFile drawedPictureFile,String params,String modelName) throws InterruptedException, ApiException, IOException, URISyntaxException {
        uploadPictureDto add = pictureService.partGenerate(pictureFile,drawedPictureFile, params, modelName);
        return Result.success(add);
    }
    @PostMapping("/add")
    public Result add(String oldPicture,String newPicture, String modelName) throws InterruptedException, ApiException, IOException, URISyntaxException {
        uploadPictureDto add = pictureService.add(oldPicture, newPicture, modelName);
        return Result.success(add);
    }
    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        pictureService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        pictureService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Picture user = pictureService.selectById(id);
        return Result.success(user);
    }

    /**
     * 查询所有
     */
    @PostMapping("/selectAll")
    public Result selectAll(@RequestBody QuaryPictureDto quaryPictureDto) throws NoSuchMethodException {
        List<QuaryPictureDto> list = pictureService.selectAll(quaryPictureDto);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/selectPage")
    public Result selectPage(@RequestBody QuaryPictureDto quaryPictureDto,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<QuaryPictureDto> userPage = pictureService.selectPage(quaryPictureDto, pageNum, pageSize);
        return Result.success(userPage);
    }
    @GetMapping("/time")
    public Result getTime() {
        List<QuaryPictureTime> time = pictureService.getTime();
        return Result.success(time);
    }

    @GetMapping("/getMoney")
    public Result getMoney() {
        List<QuaryPictureMoney> money = pictureService.getMoney();
        return Result.success(money);
    }
}
