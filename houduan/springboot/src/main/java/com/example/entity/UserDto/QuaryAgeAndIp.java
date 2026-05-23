package com.example.entity.UserDto;

import lombok.Data;

@Data
public class QuaryAgeAndIp {
    private String userAge;
    /**
     * 用户地址
     */
    private String userIp;

    public QuaryAgeAndIp(String userAge, String userIp) {
        this.userAge = userAge;
        this.userIp = userIp;
    }
}
