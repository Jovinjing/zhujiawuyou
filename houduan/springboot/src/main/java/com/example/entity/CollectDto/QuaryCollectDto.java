package com.example.entity.CollectDto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName collect
 */
@TableName(value ="collect")
@Data
public class QuaryCollectDto {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 图片
     */
    private String picture;

    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String userName;

}