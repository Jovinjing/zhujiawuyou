package com.example.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Collect;
import com.example.entity.Sharedpicture;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CollectMapper extends BaseMapper<Collect>, MPJBaseMapper<Collect> {
    @Select("SELECT Collect.*, user.user_name FROM Collect, user " +
            "WHERE Collect.user_id = user.id AND Collect.id = #{id}")
    Collect selectById(Integer id);

//    @Select("SELECT Collect.*, user.user_name " +
//            "FROM Collect " +
//            "INNER JOIN user ON Collect.user_id = user.id and user.user_name like concat('%',#{key},'%') ")
//    List<Collect> selectAll(String key);
}
