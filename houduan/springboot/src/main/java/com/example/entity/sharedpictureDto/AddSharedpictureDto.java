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
public class AddSharedpictureDto {



    private String pictureDescription;
    private Integer pictureId;



}