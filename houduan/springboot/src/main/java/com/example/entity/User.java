package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 创造时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户地址
     */
    private String userIp;

    /**
     * 关注数
     */
    private Integer attentionNum;
    private Integer praiseandcollectNum;
    private Integer fanNum;
    private String userAge;
    private String userGender;

}