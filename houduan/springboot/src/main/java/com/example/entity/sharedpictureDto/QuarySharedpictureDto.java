package com.example.entity.sharedpictureDto;

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
public class QuarySharedpictureDto {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userName;
    private String pictureDescription;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 图片
     */
    private String picture;
    private String style;
    private String roomStyle;
    /**
     * 点赞数
     */
    private Integer praiseNum;

    /**
     * 被收藏数
     */
    private Integer collectNum;
    private Integer pictureId;
    private Boolean isPraise;
}