package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.entity.AddSharedpictureDto;
import com.example.entity.Sharedpicture;
import com.example.entity.User;
import com.example.entity.pictureDto.QuaryPictureDto;
import com.example.entity.sharedpictureDto.QuarySharedpictureDto;
import com.example.service.SharedPictureService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/SharedPicture")
public class SharedPictureController {

    @Resource
    private SharedPictureService SharedPictureService;
    @Autowired
    private UserService userService;

//    @GetMapping("/praise/{id}")
//    public Result praise(@PathVariable Integer id) {
//        SharedPictureService.praise(id);
//        return Result.success("添加成功");
//    }
    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody AddSharedpictureDto addSharedpictureDto) {
        SharedPictureService.addSharedPicture(addSharedpictureDto);
        return Result.success("添加成功");
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        SharedPictureService.deleteById(id);
        return Result.success("删除成功");
    }
    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Sharedpicture sharedpicture) {
        SharedPictureService.updateById(sharedpicture);
        return Result.success("更新成功");
    }
    /**
     * 修改
     */
    @PutMapping("/parisePicture")
    public Result parisePicture(@RequestBody Sharedpicture sharedpicture) {
        SharedPictureService.parisePicture(sharedpicture);
        return Result.success("更新成功");
    }


    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        SharedPictureService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Sharedpicture sharedpicture = SharedPictureService.selectById(id);
        return Result.success(sharedpicture);
    }

    /**
     * 查询所有
     */
    @PostMapping("/selectAll")
public Result selectAll(@RequestBody  QuarySharedpictureDto quarySharedpictureDto) {
        List<QuarySharedpictureDto> list = SharedPictureService.selectAll(quarySharedpictureDto);
        return Result.success(list);
    }
//
    /**
     * 分页查询
     */
    @PostMapping("/selectPage")
    public Result selectPage(@RequestBody QuarySharedpictureDto quarySharedpictureDto,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<QuarySharedpictureDto> userPage = SharedPictureService.selectPage(quarySharedpictureDto, pageNum, pageSize);
        return Result.success(userPage);
    }
    @PostMapping("/recommend")
    public Result recommend() {
        List<QuarySharedpictureDto> list =SharedPictureService.recommend();
        return Result.success(list);
    }

}
