package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName sharedpicture
 */
@TableName(value ="sharedpicture")
@Data
public class Sharedpicture {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 图片
     */
    private String picture;

    /**
     * 点赞数
     */
    private Integer praiseNum;

    /**
     * 被收藏数
     */
    private Integer collectNum;
    private String pictureDescription;
    private Integer pictureId;


}