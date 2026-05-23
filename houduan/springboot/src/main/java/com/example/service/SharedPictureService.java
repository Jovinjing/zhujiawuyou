package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.AddSharedpictureDto;
import com.example.entity.Sharedpicture;
import com.example.entity.sharedpictureDto.QuarySharedpictureDto;

import java.util.List;

public interface SharedPictureService {
// 添加共享图片
    void addSharedPicture(AddSharedpictureDto addSharedpictureDto);

    void deleteById(Integer id);

    void updateById(Sharedpicture sharedpicture);

    Sharedpicture selectById(Integer id);

    List<QuarySharedpictureDto> selectAll(QuarySharedpictureDto quarySharedpictureDto);

    IPage<QuarySharedpictureDto> selectPage(QuarySharedpictureDto quarySharedpictureDto, Integer pageNum, Integer pageSize);

    void deleteBatch(List<Integer> ids);

    void parisePicture(Sharedpicture sharedpicture);

    List<QuarySharedpictureDto> recommend();
}
