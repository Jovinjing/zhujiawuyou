package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RelateDTO {
    /** 用户id */
    private Integer useId;
    /** 商品id */
    private Integer sharedpictureId;
    /** 指数 */
    private Integer index;

}