package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Notice;
import com.example.entity.User;
import com.example.entity.UserDto.QuaryUserDto;

import java.util.List;

public interface NoticeService {
    void add(Notice notice);

    void updateById(Notice notice);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

    Notice selectById(Integer id);

    List<Notice> selectAll(Notice notice);

    IPage<Notice> selectPage(Notice notice, Integer pageNum, Integer pageSize);
}
