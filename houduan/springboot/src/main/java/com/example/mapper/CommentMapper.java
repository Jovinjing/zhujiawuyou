package com.example.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Comment;
import com.example.entity.Sharedpicture;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 林泽楷
* @description 针对表【comment(评论)】的数据库操作Mapper
* @createDate 2025-03-11 09:08:24
* @Entity generator.domain.Comment
*/
@Mapper
@Repository

public interface CommentMapper extends BaseMapper<Comment>, MPJBaseMapper<Comment> {

    @Select("SELECT comment.*, user.user_name,sharedpicture.picture FROM comment, user,sharedpicture " +
            "WHERE comment.comment_userId = user.id AND comment.id = #{id} and comment.comment_pictureId = sharedpicture.id")
    Comment selectById(Integer id);
    @Select("SELECT comment.*, user.user_name,sharedpicture.picture " +
            "FROM comment " +
            "INNER JOIN user ON comment.comment_userId = user.id and user.user_name like concat('%',#{key},'%')"+
            "INNER JOIN sharedpicture ON comment.comment_pictureId = sharedpicture.id")
    IPage<Comment> selectPageAll(IPage<Comment> page, LambdaQueryWrapper<Comment> wrapper, String key);
    @Select("SELECT comment.*, user.user_name,sharedpicture.picture " +
            "FROM comment " +
            "INNER JOIN user ON comment.comment_userId = user.id and user.user_name like concat('%',#{key},'%')"+
            "INNER JOIN sharedpicture ON comment.comment_pictureId = sharedpicture.id")
    List<Comment> selectAll(String key);
}




