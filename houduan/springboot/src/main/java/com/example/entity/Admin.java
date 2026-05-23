package com.example.entity;

import lombok.Data;

@Data
public class Admin extends Account {
    /** 主键ID */
    private Integer id;
    /** 账号 */
    private String adminName;
    private String password;
    private String account;
    private String adminAvatar;

}
