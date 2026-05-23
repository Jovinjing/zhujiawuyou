package com.example.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.*;
import com.example.entity.AddSharedpictureDto;
import com.example.entity.pictureDto.QuaryPictureDto;
import com.example.entity.sharedpictureDto.QuarySharedpictureDto;
import com.example.exception.CustomException;
import com.example.mapper.*;
import com.example.service.SharedPictureService;
import com.example.utils.TokenUtils;
import com.example.utils.UserCF;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class SharedPictureServiceImpl implements SharedPictureService {
    @Resource
    private SharedPictureMapper sharedPictureMapper;
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private PictureMapper PictureMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserMapper userMapper;

    @Override

    public void addSharedPicture(AddSharedpictureDto addSharedpictureDto) {
        User currentUser = TokenUtils.getCurrentUser();
        Sharedpicture sharedpicture = new Sharedpicture();
        Picture picture = PictureMapper.selectById(addSharedpictureDto.getPictureId());
        if(picture.getIsShare()==1){
            throw new CustomException(ResultCodeEnum.SHARED_ERROR);
        }
        sharedpicture.setUserId(currentUser.getId());
        sharedpicture.setPictureId(addSharedpictureDto.getPictureId());
        sharedpicture.setPicture(picture.getNewPicture());
        sharedpicture.setPictureDescription(addSharedpictureDto.getPictureDescription());
        int insert = sharedPictureMapper.insert(sharedpicture);
        picture.setIsShare(1);
        PictureMapper.updateById(picture);
        if(insert != 1){
            throw new CustomException(ResultCodeEnum.INSERT_ERROR);
        }
    }

    @Override
    public void deleteById(Integer id) {
        sharedPictureMapper.deleteById(id);
    }

    @Override
    public void updateById(Sharedpicture sharedpicture) {
        sharedPictureMapper.updateById(sharedpicture);
    }

    @Override
    public Sharedpicture selectById(Integer id) {
        Sharedpicture sharedpicture = sharedPictureMapper.selectById(id);
        return sharedpicture;
    }

    @Override
    public List<QuarySharedpictureDto> selectAll(QuarySharedpictureDto quarySharedpictureDto) {
        //queryWrapper组装查s询where条件
        MPJLambdaWrapper<Sharedpicture> mpjLambdaWrapper = JoinWrappers.lambda(Sharedpicture.class);
        mpjLambdaWrapper.selectAll(Sharedpicture.class)
                .select(User::getUserName)  // 查询 Role 表的 name 字段
                .select(Picture::getStyle)  // 查询 Role 表的 name 字段
                .select(Picture::getRoomStyle)  // 查询 Role 表的 name 字段
                .leftJoin(User.class,User::getId,Sharedpicture::getUserId)
                .leftJoin(Picture.class,Picture::getId,Sharedpicture::getPictureId)
                .like(quarySharedpictureDto.getUserName()!=null,User::getUserName, quarySharedpictureDto.getUserName())
                .like(quarySharedpictureDto.getPictureDescription()!=null,Sharedpicture::getPictureDescription, quarySharedpictureDto.getPictureDescription());
        System.out.println(quarySharedpictureDto.getId());// 关联 user_role
        //连表查询 返回自定义ResultType
        List<QuarySharedpictureDto> sharedpictures = sharedPictureMapper.selectJoinList(QuarySharedpictureDto.class, mpjLambdaWrapper);
       sharedpictures.stream().map(item -> {
             item.setIsPraise(isPictureLiked(item.getId()));
            return null;
        }).toList();
        System.out.println(sharedpictures);
        return sharedpictures;
    }

    private boolean isPictureLiked(int id) {
        User currentUser = TokenUtils.getCurrentUser();
        Integer userId = currentUser.getId();
        String field=userId+":"+id;
        Boolean praiseStatus = redisTemplate.opsForHash().hasKey("praiseStatus", field);
        if(praiseStatus){
            return true;
        }
        return false;
    }

    @Override
    public IPage<QuarySharedpictureDto> selectPage(QuarySharedpictureDto quarySharedpictureDto, Integer pageNum, Integer pageSize) {
        IPage<QuarySharedpictureDto> page = new Page<>(pageNum, pageSize);
        //queryWrapper组装查s询where条件
        MPJLambdaWrapper<Sharedpicture> mpjLambdaWrapper = JoinWrappers.lambda(Sharedpicture.class);
        mpjLambdaWrapper.selectAll(Sharedpicture.class)
                .select(User::getUserName)  // 查询 Role 表的 name 字段
                .select(Picture::getStyle)  // 查询 Role 表的 name 字段
                .select(Picture::getRoomStyle)  // 查询 Role 表的 name 字段

                .leftJoin(Picture.class,Picture::getId,Sharedpicture::getPictureId)

                .leftJoin(User.class,User::getId,Sharedpicture::getUserId)
                .like(quarySharedpictureDto.getUserName()!=null,User::getUserName, quarySharedpictureDto.getUserName())
                .like(quarySharedpictureDto.getPictureDescription()!=null,Sharedpicture::getPictureDescription, quarySharedpictureDto.getPictureDescription());
        //分页查询 （需要启用 mybatis plus 分页插件）
        IPage<QuarySharedpictureDto> listPage = sharedPictureMapper.selectJoinPage(page, QuarySharedpictureDto.class, mpjLambdaWrapper);
        return listPage;
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        sharedPictureMapper.deleteByIds(ids);
    }

    @Override
    public void parisePicture(Sharedpicture sharedpicture) {
        Sharedpicture sharedpicture1 = sharedPictureMapper.selectById(sharedpicture.getId());
        //获取当前用户id
        User currentUser = TokenUtils.getCurrentUser();
        Integer userId = currentUser.getId();
        Integer id=sharedpicture.getId();
        String field=userId+":"+id;
        //判断有没有点赞
        Boolean praiseStatus = redisTemplate.opsForHash().hasKey("praiseStatus", field);
        if(praiseStatus){
            //有就取消点赞
            redisTemplate.opsForHash().delete("praiseStatus", field);
            sharedpicture.setPraiseNum(sharedpicture1.getPraiseNum()-1);
            currentUser.setPraiseandcollectNum(currentUser.getPraiseandcollectNum()-1);
        }else{
            //没有就点赞并让对应的图片点赞数加1
            redisTemplate.opsForHash().put("praiseStatus", field, 1);
            sharedpicture.setPraiseNum(sharedpicture1.getPraiseNum()+1);
            currentUser.setPraiseandcollectNum(currentUser.getPraiseandcollectNum()+1);
        }
        sharedPictureMapper.updateById(sharedpicture);
        userMapper.updateById(currentUser);
    }

    @Override
    public List<QuarySharedpictureDto> recommend() {
        User currentUser = TokenUtils.getCurrentUser();

        QuarySharedpictureDto quarySharedpictureDto = new QuarySharedpictureDto();
        // 用户的哪些行为可以认为他跟商品产生了关系？收藏、加入购物车、下单、评论
        // 1. 获取所有的收藏信息
        List<Collect> allCollects = collectMapper.selectList(null);
        // 4. 获取所有的评论信息
        List<Comment> allComments = commentMapper.selectList(null);
        // 5. 获取所有的用户信息
        List<User> allUsers = userMapper.selectList(null);
        // 6. 获取所有的商品信息
        List<QuarySharedpictureDto> allSharedPicture =selectAll(quarySharedpictureDto);
        List<QuarySharedpictureDto> recommendResult;
        List<RelateDTO> data = new ArrayList<>();

        for (QuarySharedpictureDto sharedpicture: allSharedPicture) {
            Integer sharedpictureId = sharedpicture.getId();
            for (User user:allUsers) {
                Integer userid = user.getId();
                String field=userid+":"+sharedpictureId;
                int index=1;
                // 1. 判断该用户有没有收藏该商品，收藏的权重我们给 1
                Optional<Collect> collectoptional = allCollects.stream().filter(x -> x.getPictureId().equals(sharedpictureId) && x.getUserId().equals(userid)).findFirst();
                if(collectoptional.isPresent()){
                    index+=1;
                }
                // 4. 判断该用户有没有对该商品进行评论，评论的权重我们给 4
                for (Comment comment:allComments) {
                    if(comment.getCommentUserid().equals(userid) && comment.getCommentPictureid().equals(sharedpictureId)){
                        index+=2;
                    }
                }
                //判断有没有点赞
                Boolean praiseStatus = redisTemplate.opsForHash().hasKey("praiseStatus", field);
                if(praiseStatus){
                    index+=3;
                }
                if (index > 1) {
                    RelateDTO relateDTO = new RelateDTO(userid, sharedpictureId, index);
                    System.out.println(relateDTO);

                    data.add(relateDTO);
                }


            }
        }
        List<Integer> sharedpictureIds = UserCF.recommend(currentUser.getId(), data);

        recommendResult = sharedpictureIds.stream().map(goodsId -> allSharedPicture.stream()
                        .filter(x -> x.getId().equals(goodsId)).findFirst().orElse(null))
                .toList();
        List<QuarySharedpictureDto> list = allSharedPicture.stream().filter(g -> !sharedpictureIds.contains(g.getId())).toList();
        ArrayList<QuarySharedpictureDto> newrecommendResult = new ArrayList<>(recommendResult);
        newrecommendResult.addAll(list);

        return newrecommendResult;
    }

}
