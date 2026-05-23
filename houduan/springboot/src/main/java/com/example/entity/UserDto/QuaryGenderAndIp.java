package com.example.entity.UserDto;

import lombok.Data;

@Data
public class QuaryGenderAndIp {
    private String userGender;
    /**
     * 用户地址
     */
    private String userIp;

    public QuaryGenderAndIp(String userGender, String userIp) {
        this.userGender = userGender;
        this.userIp = userIp;
    }
}
