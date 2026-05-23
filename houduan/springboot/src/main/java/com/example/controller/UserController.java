package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Admin;
import com.example.entity.Login;
import com.example.entity.User;
import com.example.entity.UserDto.QuaryAgeAndIp;
import com.example.entity.UserDto.QuaryGenderAndIp;
import com.example.entity.UserDto.QuaryUserDto;
import com.example.service.UserService;
import com.example.service.impl.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody Login login) {
        User user = userService.userLogin(login);
        return Result.success(user);
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        userService.add(user);
        return Result.success("添加成功");
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        userService.updateById(user);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        userService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        userService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        User user = userService.selectById(id);
        return Result.success(user);
    }

    /**
     * 查询所有
     */
    @PostMapping("/selectAll")
    public Result selectAll(@RequestBody User user) {
        List<QuaryUserDto> list = userService.selectAll(user);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/selectPage")
    public Result selectPage(@RequestBody User user,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<User> userPage = userService.selectPage(user, pageNum, pageSize);
        return Result.success(userPage);
    }
    @PostMapping("/attention")
    public Result attentionUser(@RequestBody User user) {
        userService.attentionUser(user);
        return Result.success();
    }
    @GetMapping("/ip")
    public Result userIp() {
        List<String> users = userService.userIp();
        return Result.success(users);
    }
  @GetMapping("/age")
    public Result userAge() {
        List<QuaryAgeAndIp> users = userService.userAge();
        return Result.success(users);
    }
  @GetMapping("/gender")
    public Result userGender() {
        List<QuaryGenderAndIp> users = userService.userGender();
        return Result.success(users);
    }
    @GetMapping("/attentionUser")
    public Result getAttentionUser() {
        List<Integer> users = userService.getattentionUser();
        return Result.success(users);
    }
    @GetMapping("/attentionedUser")
    public Result getAttentionedUser() {
        List<Integer> users = userService.getattentionedUser();
        return Result.success(users);
    }
}
