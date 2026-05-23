package com.example.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Comment;
import com.example.entity.CommentDto.QuaryCommentDto;
import com.example.entity.Picture;
import com.example.entity.Sharedpicture;
import com.example.entity.User;
import com.example.entity.pictureDto.QuaryPictureDto;
import com.example.entity.sharedpictureDto.QuarySharedpictureDto;
import com.example.exception.CustomException;
import com.example.mapper.CommentMapper;
import com.example.service.CommentService;
import com.example.utils.TokenUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
* @author 林泽楷
* @description 针对表【comment(评论)】的数据库操作Service实现
* @createDate 2025-03-11 09:08:24
*/
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;
    @Override
    public void add(Comment comment) {
        User currentUser = TokenUtils.getCurrentUser();
        comment.setCommentUserid(currentUser.getId());
        comment.setCommentTime(DateUtil.now());
        int insert = commentMapper.insert(comment);
        if (insert == 0) {
            throw new CustomException(ResultCodeEnum.INSERT_ERROR);
        }
    }

    @Override
    public void deleteById(Integer id) {
        commentMapper.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        commentMapper.deleteByIds(ids);
    }

    @Override
    public Comment selectById(Integer id) {
        Comment comment = commentMapper.selectById(id);
        return comment;
    }

    @Override
    public List<QuaryCommentDto> selectAll(QuaryCommentDto quaryCommentDto) {
        //queryWrapper组装查s询where条件
        MPJLambdaWrapper<Comment> mpjLambdaWrapper = JoinWrappers.lambda(Comment.class);
        mpjLambdaWrapper.selectAll(Comment.class)
                .select(User::getUserName)  // 查询 Role 表的 name 字段
                .leftJoin(User.class,User::getId,Comment::getCommentUserid) // 关联 user_role
                .eq(quaryCommentDto.getCommentUserid()!=null,Comment::getCommentUserid,quaryCommentDto.getCommentUserid())
                .eq(quaryCommentDto.getCommentPictureid()!=null,Comment::getCommentPictureid,quaryCommentDto.getCommentPictureid())
                .like(quaryCommentDto.getUserName()!=null,User::getUserName, quaryCommentDto.getUserName());
        //连表查询 返回自定义ResultType
        List<QuaryCommentDto> sharedpictures = commentMapper.selectJoinList(QuaryCommentDto.class, mpjLambdaWrapper);
        return sharedpictures;
    }

    @Override
    public void updateById(Comment comment) {
        int i = commentMapper.updateById(comment);
        if(i != 1){
            throw new CustomException(ResultCodeEnum.UPDATE_ERROR);
        }
    }

    @Override
    public IPage<QuaryCommentDto> selectPage(QuaryCommentDto quaryCommentDto, Integer pageNum, Integer pageSize) {
        IPage<QuaryCommentDto> page = new Page<>(pageNum, pageSize);

        //queryWrapper组装查s询where条件
        MPJLambdaWrapper<Comment> mpjLambdaWrapper = JoinWrappers.lambda(Comment.class);
        mpjLambdaWrapper.selectAll(Comment.class)
                .select(User::getUserName)  // 查询 Role 表的 name 字段
                .select(Sharedpicture::getPicture)  // 查询 Role 表的 name 字段
                .leftJoin(User.class,User::getId,Comment::getCommentUserid) // 关联 user_role
                .leftJoin(Sharedpicture.class,Sharedpicture::getId,Comment::getCommentPictureid) // 关联 user_role
                .like(User::getUserName, quaryCommentDto.getUserName());
        //分页查询 （需要启用 mybatis plus 分页插件）
        IPage<QuaryCommentDto> commentPage = commentMapper.selectJoinPage(page, QuaryCommentDto.class, mpjLambdaWrapper);
        return commentPage;
    }
}




