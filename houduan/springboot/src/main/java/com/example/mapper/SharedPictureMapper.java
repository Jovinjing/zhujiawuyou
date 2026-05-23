package com.example.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Picture;
import com.example.entity.Sharedpicture;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SharedPictureMapper extends BaseMapper<Sharedpicture>, MPJBaseMapper<Sharedpicture> {

}
