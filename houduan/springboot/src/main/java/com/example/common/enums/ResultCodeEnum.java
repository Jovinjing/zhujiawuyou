package com.example.common.enums;

public enum ResultCodeEnum {

    SUCCESS("200", "成功"),
    PARAM_ERROR("400", "参数异常"),
    TOKEN_INVALID_ERROR("401", "无效的token"),
    TOKEN_CHECK_ERROR("401", "token验证失败，请重新登录"),
    PARAM_LOST_ERROR("4001", "参数缺失"),

    SYSTEM_ERROR("500", "系统异常"),
    USER_EXIST_ERROR("5001", "用户已存在"),
    USER_NOT_LOGIN("5002", "用户未登录"),
    USER_ACCOUNT_ERROR("5003", "账号或密码错误"),
    USER_NOT_EXIST_ERROR("5004", "用户不存在"),
    PARAM_PASSWORD_ERROR("5005", "原密码输入错误"),
    GET_TOKEN_ERROR("5006", "获取access_token失败"),
    GET_PHONE_ERROR("5007", "获取手机号失败"),
    NO_AUTH_ERROR("5008", "无权限访问"),
    INSERT_ERROR("5009", "插入失败"),
    UPDATE_ERROR("5010", "更新异常"),
    PHONE_EMPTY_ERROR("5012", "手机号不能为空"),
    DELETE_ERROR("5011", "删除异常"),
    COLLECT_ALREADY_ERROR("5012", "已收藏"),
    NOT_FAN_ERROR("5013","没有粉丝" ),
    SHARED_ERROR("5015","已经分享"),
    NOT_ATTENTION_ERROR("5014","没有关注");

    public String code;
    public String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
