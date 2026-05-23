package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Comment;
import com.example.entity.CommentDto.QuaryCommentDto;
import com.example.entity.Picture;

import java.util.List;

/**
* @author 林泽楷
* @description 针对表【comment(评论)】的数据库操作Service
* @createDate 2025-03-11 09:08:24
*/
public interface CommentService{
    void add(Comment comment);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

    Comment selectById(Integer id);

// 根据关键字查询所有评论
    List<QuaryCommentDto> selectAll(QuaryCommentDto quaryCommentDto);
    void updateById(Comment comment);

    IPage<QuaryCommentDto> selectPage(QuaryCommentDto quaryCommentDto, Integer pageNum, Integer pageSize);
}
