package com.example.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Notice;
import com.example.entity.User;
import com.example.entity.UserDto.QuaryUserDto;
import com.example.exception.CustomException;
import com.example.mapper.NoticeMapper;
import com.example.service.NoticeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NoticeServiceImpl implements NoticeService {
    @Resource
    private NoticeMapper noticeMapper;

    @Override
    public void add(Notice notice) {
        notice.setCreateTime(DateUtil.now());
        int insert =noticeMapper.insert(notice);
        if(insert != 1){
            throw new CustomException(ResultCodeEnum.INSERT_ERROR);
        }
    }

    @Override
    public void updateById(Notice notice) {
        notice.setCreateTime(DateUtil.now());
        int i = noticeMapper.updateById(notice);
        if(i != 1){
            throw new CustomException(ResultCodeEnum.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteById(Integer id) {
        noticeMapper.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        noticeMapper.deleteByIds(ids);
    }

    @Override
    public Notice selectById(Integer id) {
        Notice notice = noticeMapper.selectById(id);

        return notice;
    }

    @Override
    public List<Notice> selectAll(Notice notice) {
        QueryWrapper<Notice> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.like(notice.getContent() != null, "content", notice.getContent());
        List<Notice> notices = noticeMapper.selectList(objectQueryWrapper);
        return notices;
    }

    @Override
    public IPage<Notice> selectPage(Notice notice, Integer pageNum, Integer pageSize) {
        IPage<Notice> page = new Page<>(pageNum, pageSize);
        //queryWrapper组装查询where条件
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Notice::getContent,notice.getContent());
        IPage<Notice> noticePage = noticeMapper.selectPage(page, queryWrapper);
        return noticePage;
    }
}
