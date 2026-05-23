package com.example.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Picture;
import com.example.entity.User;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 林泽楷
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-03-11 09:08:24
* @Entity generator.domain.User
 *
*/
@Mapper
public interface PictureMapper extends BaseMapper<Picture>, MPJBaseMapper<Picture> {


    Picture selectById(Integer id);

    IPage<Picture> selectPageAll(@Param("") IPage<Picture> page,@Param("")  Wrapper<Picture> wrapper,Picture picture);
    @Select("SELECT picture.*, user.user_name " +
            "FROM picture " +
            "INNER JOIN user ON picture.user_id = user.id and user.user_name like concat('%',#{key},'%')")
    List<Picture> selectAll(Picture picture);
}




