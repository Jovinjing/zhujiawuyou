package com.example.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.User;
import com.example.entity.Login;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.example.service.WebService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class WebServiceImpl implements WebService {

}
