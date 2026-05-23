package com.example.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.config.RedisConfig;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Login;
import com.example.entity.Sharedpicture;
import com.example.entity.User;
import com.example.entity.UserDto.QuaryAgeAndIp;
import com.example.entity.UserDto.QuaryGenderAndIp;
import com.example.entity.UserDto.QuaryUserDto;
import com.example.entity.sharedpictureDto.QuarySharedpictureDto;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.utils.TokenUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.tools.Tool;
import java.time.LocalDateTime;
import java.util.*;

/**
* @author 林泽楷
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-03-11 09:08:24
*/
@Service
public class UserServiceImpl  implements UserService {

    @Value("${wx.appid}")
    private  String appid;
    @Value("${wx.secret}")
    private  String secret;
    private static final String BASE_URL = "https://api.weixin.qq.com";
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate redisTemplate;


    @Override
    public User getLoginUser(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        User user = userMapper.selectById(userId);
        return user;
    }
    @Override
    public User userLogin(Login login) {

//        2.获取手机号凭证

        String accessToken = getAccessToken();
        if(accessToken==null){
            throw new CustomException(ResultCodeEnum.GET_TOKEN_ERROR);
        }
//        3.获取手机号
        String phone = getPhone(login.getPhoneCode(), accessToken);
        if(phone==null){
            throw new CustomException(ResultCodeEnum.GET_PHONE_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_phone", phone);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            User user1 = new User();
            user1.setUserAvatar(" ");
            user1.setCreateTime(DateUtil.now());
            user1.setUpdateTime(DateUtil.now());
            user1.setUserPhone(phone);
            user1.setUserName("匿名用户");
            user1.setUserIp(" ");

            userMapper.insert(user1);
            User user2 = userMapper.selectOne(queryWrapper);

//            // 生成token
//            String token = TokenUtils.createToken(user1.getId() + "-" + dbAdmin.getRole(), dbAdmin.getPassword());
//            user1.setToken(token);
            return user2;
        }
        else {
            return user;
        }
    }

    @Override
    public void add(User user) {
        if(user.getUserPhone()== null){
            throw new CustomException(ResultCodeEnum.PHONE_EMPTY_ERROR);
        }
        if(userMapper.selectOne(new QueryWrapper<User>().eq("user_phone",user.getUserPhone())) != null){
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        user.setCreateTime(DateUtil.now());
        user.setUpdateTime(DateUtil.now());
        int insert = userMapper.insert(user);
        if(insert != 1){
            throw new CustomException(ResultCodeEnum.INSERT_ERROR);
        }
    }

    @Override
    public void updateById(User user) {
        user.setUpdateTime(DateUtil.now());
        int i = userMapper.updateById(user);
        if(i != 1){
            throw new CustomException(ResultCodeEnum.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        userMapper.deleteByIds(ids);
    }

    @Override
    public User selectById(Integer id) {
        User user = userMapper.selectById(id);
        return user;
    }

    @Override
    public IPage<User> selectPage(User user,Integer pageNum,Integer pageSize) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        //queryWrapper组装查询where条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getUserName,user.getUserName());
        IPage<User> userPage = userMapper.selectPage(page, queryWrapper);
        return userPage;
    }

    @Override
    public void attentionUser(User attentionedUser) {
        User user = userMapper.selectById(attentionedUser.getId());
        User currentUser = TokenUtils.getCurrentUser();
        User currentUser1 = currentUser;
        //判断有没有关注
        Boolean attentionStatus = redisTemplate.opsForSet().isMember("userid:"+ currentUser.getId(),attentionedUser.getId() );
        //没有则关注 有则取关 关注数减一
        if(attentionStatus){
            System.out.println("已经关注");
            redisTemplate.opsForSet().remove("userid:"+ currentUser.getId(),attentionedUser.getId());
            attentionedUser.setFanNum(user.getFanNum()-1);
            currentUser1.setAttentionNum(currentUser.getAttentionNum()-1);
        }else {
            System.out.println("未关注");
            redisTemplate.opsForSet().add("userid:"+ currentUser.getId(),attentionedUser.getId());
            redisTemplate.opsForSet().add("attentionedUserid:"+ attentionedUser.getId(),currentUser.getId());
            attentionedUser.setFanNum(user.getFanNum()+1);
            currentUser1.setAttentionNum(currentUser.getAttentionNum()+1);

        }
        userMapper.updateById(attentionedUser);
        userMapper.updateById(currentUser1);
    }

    @Override
    public List<String> userIp() {
        QueryWrapper<User> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.select("user_ip");
        List<User> users = userMapper.selectList(objectQueryWrapper);
        ArrayList<String> objects = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            objects.add(users.get(i).getUserIp());
        }
        return objects;
    }

    @Override
    public List<QuaryAgeAndIp> userAge() {
        QueryWrapper<User> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.select("user_ip","user_age");
        List<User> users = userMapper.selectList(objectQueryWrapper);
        ArrayList<QuaryAgeAndIp> objects = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            QuaryAgeAndIp quaryAgeAndIp = new QuaryAgeAndIp(users.get(i).getUserAge(),users.get(i).getUserIp());
            objects.add(quaryAgeAndIp);
        }
        return objects;
    }

    @Override
    public List<QuaryGenderAndIp> userGender() {
        QueryWrapper<User> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.select("user_ip","user_gender");
        List<User> users = userMapper.selectList(objectQueryWrapper);
        ArrayList<QuaryGenderAndIp> objects = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            QuaryGenderAndIp quaryGenderAndIp = new QuaryGenderAndIp(users.get(i).getUserGender(),users.get(i).getUserIp());
            objects.add(quaryGenderAndIp);
        }
        return objects;
    }

    @Override
    public List<Integer> getattentionUser() {
        User currentUser = TokenUtils.getCurrentUser();

        Boolean b = redisTemplate.hasKey("userid:" + currentUser.getId());
        if(!b){
            throw new CustomException(ResultCodeEnum.NOT_ATTENTION_ERROR);
        }
        Set<Integer> members = redisTemplate.opsForSet().members("userid:" + currentUser.getId());
        System.out.println(members);
        return new ArrayList<>(members);
    }

    @Override
    public List<Integer> getattentionedUser() {
        User currentUser = TokenUtils.getCurrentUser();
        Boolean b = redisTemplate.hasKey("attentionedUserid:" + currentUser.getId());
        if(!b){
            throw new CustomException(ResultCodeEnum.NOT_FAN_ERROR);
        }
        Set<Integer> members = redisTemplate.opsForSet().members("attentionedUserid:" + currentUser.getId());
        System.out.println(members);
        return new ArrayList<>(members);
    }

    @Override
    public List<QuaryUserDto> selectAll(User user) {
        System.out.println(user);
        MPJLambdaWrapper<User> mpjLambdaWrapper = JoinWrappers.lambda(User.class);
        mpjLambdaWrapper.like(User::getUserName, user.getUserName());
        List<QuaryUserDto> quaryUserDto = userMapper.selectJoinList(QuaryUserDto.class, mpjLambdaWrapper);
        quaryUserDto.stream().map(item -> {
            System.out.println(isUserAttention(item.getId()));
            item.setIsAttention(isUserAttention(item.getId()));
            return null;
        }).toList();
        System.out.println(quaryUserDto);
        return quaryUserDto;
    }
    private boolean isUserAttention(int id) {
        User currentUser = TokenUtils.getCurrentUser();
        Integer userId = currentUser.getId();
        String key="userid:"+userId;
        System.out.println(key);
        Boolean attentionStatus = redisTemplate.opsForSet().isMember(key, id);
        if(attentionStatus){
            return true;
        }
        return false;
    }

    private String getAccessToken() {
        String url = "/cgi-bin/token";
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", appid);
        params.put("secret", secret);
        String token = null;
        try {
            String resStr = HttpUtil.get(BASE_URL+url, params);
            token = (String) JSONUtil.parseObj(resStr).get("access_token");
        } catch (Exception e) {
            throw new CustomException(ResultCodeEnum.GET_TOKEN_ERROR);
        }
        return token;
    }
    private String getPhone(String phoneCode, String accessToken) {
        String url = "/wxa/business/getuserphonenumber";
        Map<String, Object> bodys = new HashMap<>();
        System.out.println(phoneCode);
        System.out.println(accessToken);
        bodys.put("code", phoneCode);
        String phone = null;
        try {
            HttpRequest request = HttpUtil.createRequest(Method.POST, BASE_URL + url+"?access_token="+accessToken);
            request.body(JSONUtil.toJsonStr(bodys));
            System.out.println(JSONUtil.toJsonStr(bodys));
            HttpResponse response = request.execute();
            String resStr =response.body();
            System.out.println(resStr);

            Object phoneInfo = JSONUtil.parseObj(resStr). get("phone_info");
            System.out.println(phoneInfo);
            phone = JSONUtil.parseObj(phoneInfo).getStr("phoneNumber");
            return phone;
        } catch (Exception e) {
            throw new CustomException(ResultCodeEnum.GET_PHONE_ERROR);
        }
    }

}




