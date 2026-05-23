package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;

/**
 * 图片
 * @TableName picture
 */
@TableName(value ="picture")
@Data
public class Picture {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 生成之前的图片
     */
    private String oldPicture;

    /**
     * 生成之后的图片
     */
    private String newPicture;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 用户id
     */
    private Integer userId;
    private Integer budget;
    private String style;
    private String roomStyle;
    /**
     * 是否分享
     */
    private Integer isShare;


}