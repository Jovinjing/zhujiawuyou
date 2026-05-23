package com.example.entity.pictureDto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class QuaryPictureDto {
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
     * 用户名
     */

    private String userName;
    private String style;
    private String roomStyle;
    /**
     * 用户id
     */
    private String isShare;
    private Integer userId;
}
