package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Sharedpicture;
import com.example.entity.User;
import com.example.entity.UserDto.QuaryUserDto;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 林泽楷
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-03-11 09:08:24
* @Entity generator.domain.User
 *
*/
@Mapper
public interface UserMapper extends BaseMapper<User>, MPJBaseMapper<User> {
    @Select("select user.id from user where user_name=#{name}")
   Integer selectIdByName(String name);
}




