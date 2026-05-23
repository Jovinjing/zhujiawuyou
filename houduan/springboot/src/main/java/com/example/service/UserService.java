package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Admin;
import com.example.entity.Login;
import com.example.entity.User;
import com.example.entity.UserDto.QuaryAgeAndIp;
import com.example.entity.UserDto.QuaryGenderAndIp;
import com.example.entity.UserDto.QuaryUserDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author 林泽楷
* @description 针对表【user】的数据库操作Service
* @createDate 2025-03-11 09:08:24
*/
public interface UserService{

    User userLogin(Login login);

    void add(User user);

    void updateById(User user);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

    User selectById(Integer id);
    User getLoginUser(HttpServletRequest request);

    List<QuaryUserDto> selectAll(User user);
    IPage<User> selectPage(User user, Integer pageNum, Integer pageSize);

    void attentionUser(User user);

// 返回一个包含objects对象的列表
    List<String> userIp();

    List<QuaryAgeAndIp> userAge();

    List<QuaryGenderAndIp> userGender();

    List<Integer> getattentionUser();

    List<Integer> getattentionedUser();
}
