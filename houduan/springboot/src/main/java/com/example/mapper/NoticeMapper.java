package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Notice;
import com.example.entity.User;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author 林泽楷
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-03-11 09:08:24
* @Entity generator.domain.User
 *
*/
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

}




