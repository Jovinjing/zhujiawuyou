package com.example.entity.CommentDto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 评论
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class QuaryCommentDto {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 评论描述
     */
    private String commentDescribe;

    /**
     * 评论时间
     */
    private String commentTime;

    /**
     * 评论用户id
     */
    private Integer commentUserid;

    /**
     * 评论图片id
     */
    private Integer commentPictureid;
    private String userName;
    private String picture;

}