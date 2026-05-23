package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.*;
import com.example.entity.CollectDto.QuaryCollectDto;
import com.example.entity.CommentDto.QuaryCommentDto;
import com.example.exception.CustomException;
import com.example.mapper.PictureMapper;
import com.example.mapper.CollectMapper;
import com.example.mapper.SharedPictureMapper;
import com.example.mapper.UserMapper;
import com.example.service.CollectService;
import com.example.utils.TokenUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class CollectServiceImpl implements CollectService {
    @Resource
    private CollectMapper CollectMapper;
    @Resource
    private PictureMapper PictureMapper;
    @Resource
    private SharedPictureMapper SharedPictureMapper;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private UserMapper userMapper;
    @Override

    public void addCollect(Integer pictureId) {
        User currentUser = TokenUtils.getCurrentUser();

        Collect Collect = new Collect();
        QueryWrapper<Collect> objectQueryWrapper = new QueryWrapper<Collect>();
        objectQueryWrapper.eq("user_id",currentUser.getId());
        objectQueryWrapper.eq("picture_id",pictureId);
        Collect collect = collectMapper.selectOne(objectQueryWrapper);
        if(collect != null){
            throw new CustomException(ResultCodeEnum. COLLECT_ALREADY_ERROR);
        }
        Sharedpicture picture = SharedPictureMapper.selectById(pictureId);
        Collect.setUserId(currentUser.getId());
        Collect.setPicture(picture.getPicture());
        Collect.setPictureId(pictureId);
        int insert = CollectMapper.insert(Collect);
        if(insert != 1){
            throw new CustomException(ResultCodeEnum.INSERT_ERROR);
        }
        Sharedpicture sharedpicture = new Sharedpicture();
        sharedpicture.setId(pictureId);
        sharedpicture.setCollectNum(picture.getCollectNum()+1);
        currentUser.setPraiseandcollectNum(currentUser.getPraiseandcollectNum()+1);
        userMapper.updateById(currentUser);
        SharedPictureMapper.updateById(sharedpicture);
    }

    @Override
    public void deleteById(Integer id) {
        Collect collect = CollectMapper.selectById(id);
        Sharedpicture sharedpicture = SharedPictureMapper.selectById(collect.getPictureId());
        sharedpicture.setCollectNum(sharedpicture.getCollectNum()-1);
        User currentUser = TokenUtils.getCurrentUser();
        currentUser.setPraiseandcollectNum(currentUser.getPraiseandcollectNum()-1);
        userMapper.updateById(currentUser);
        SharedPictureMapper.updateById(sharedpicture);
        CollectMapper.deleteById(id);
    }

    @Override
    public void updateById(Collect Collect) {
        CollectMapper.updateById(Collect);
    }

    @Override
    public Collect selectById(Integer id) {
        Collect Collect = CollectMapper.selectById(id);
        return Collect;
    }

    @Override
    public List<QuaryCollectDto> selectAll(QuaryCollectDto quaryCollectDto) {
        //queryWrapper组装查s询where条件
        MPJLambdaWrapper<Collect> mpjLambdaWrapper = JoinWrappers.lambda(Collect.class);
        mpjLambdaWrapper.selectAll(Collect.class)
                .select(User::getUserName)  // 查询 Role 表的 name 字段
                .leftJoin(User.class,User::getId,Collect::getUserId) // 关联 user_role
                .eq(quaryCollectDto.getUserId()!=null,Collect::getUserId, quaryCollectDto.getUserId())
                .like(quaryCollectDto.getUserName()!=null,User::getUserName, quaryCollectDto.getUserName());
        //连表查询 返回自定义ResultType
        List<QuaryCollectDto> sharedpictures = CollectMapper.selectJoinList(QuaryCollectDto.class, mpjLambdaWrapper);
        return sharedpictures;
    }

    @Override
    public IPage<QuaryCollectDto> selectPage(QuaryCollectDto quaryCollectDto, Integer pageNum, Integer pageSize) {
        IPage<QuaryCollectDto> page = new Page<>(pageNum, pageSize);
        //queryWrapper组装查s询where条件
        MPJLambdaWrapper<Collect> mpjLambdaWrapper = JoinWrappers.lambda(Collect.class);
        mpjLambdaWrapper.selectAll(Collect.class)
                .select(User::getUserName)  // 查询 Role 表的 name 字段
                .leftJoin(User.class,User::getId,Collect::getUserId) // 关联 user_role
                .like(quaryCollectDto.getUserName()!=null,User::getUserName, quaryCollectDto.getUserName());
        //分页查询 （需要启用 mybatis plus 分页插件）
        IPage<QuaryCollectDto> commentPage = CollectMapper.selectJoinPage(page, QuaryCollectDto.class, mpjLambdaWrapper);
        return commentPage;
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        CollectMapper.deleteByIds(ids);
    }

}
