package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Collect;
import com.example.entity.CollectDto.QuaryCollectDto;
import com.example.entity.CommentDto.QuaryCommentDto;

import java.util.List;

public interface CollectService {
    void addCollect(Integer pictureId);

    void deleteById(Integer id);

    void updateById(Collect Collect);

    Collect selectById(Integer id);

    List<QuaryCollectDto > selectAll(QuaryCollectDto quaryCollectDto);

    IPage<QuaryCollectDto> selectPage(QuaryCollectDto quaryCollectDto, Integer pageNum, Integer pageSize);

    void deleteBatch(List<Integer> ids);

}
