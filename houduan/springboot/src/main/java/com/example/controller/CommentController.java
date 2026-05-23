package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.entity.Comment;
import com.example.entity.CommentDto.QuaryCommentDto;
import com.example.entity.Notice;
import com.example.entity.User;
import com.example.service.CommentService;
import com.example.service.UserService;
import com.example.utils.TokenUtils;
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
@RequestMapping("/Comment")
public class CommentController {

    @Resource
    private CommentService CommentService;
    @Autowired
    private UserService userService;


    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Comment comment) {

        CommentService.add(comment);
        return Result.success("添加成功");
    }
    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Comment comment) {
        CommentService.updateById(comment);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        CommentService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        CommentService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Comment comment = CommentService.selectById(id);
        return Result.success(comment);
    }

    /**
     * 查询所有
     */
    @PostMapping("/selectAll")

    public Result selectAll(@RequestBody QuaryCommentDto quaryCommentDto) throws NoSuchMethodException {
        List<QuaryCommentDto> list = CommentService.selectAll(quaryCommentDto);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/selectPage")
    public Result selectPage(@RequestBody QuaryCommentDto quaryCommentDto,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<QuaryCommentDto> userPage = CommentService.selectPage(quaryCommentDto, pageNum, pageSize);
        return Result.success(userPage);
    }

}
