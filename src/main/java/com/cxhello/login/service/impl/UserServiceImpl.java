package com.cxhello.login.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxhello.login.dto.UserDto;
import com.cxhello.login.constant.JwtKeyConstants;
import com.cxhello.login.constant.RedisKeyConstants;
import com.cxhello.login.dao.UserMapper;
import com.cxhello.login.entity.LoginUser;
import com.cxhello.login.entity.User;
import com.cxhello.login.service.UserService;
import com.cxhello.login.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author cxhello
 * @date 2021/10/26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final long MILLIS_SECOND = 1000;

    private static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Value("${jwt.expireTime}")
    private int expireTime;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String login(UserDto userDto) {
        // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getPhotoNumber(), userDto.getPassword()));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String uuid = UUID.randomUUID().toString();
        loginUser.setToken(uuid);
        set(loginUser);
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtKeyConstants.LOGIN_USER_KEY, uuid);
        return jwtUtils.generateToken(claims);
    }

    @Override
    public void logout(String token) {
        stringRedisTemplate.delete(RedisKeyConstants.getLoginTokenKey(token));
    }

    @Override
    public LoginUser getLoginUser(String token) {
        if (StringUtils.isNotBlank(token)) {
            Claims claims = jwtUtils.parseToken(token);
            String uuid = (String) claims.get(JwtKeyConstants.LOGIN_USER_KEY);
            String userKey = RedisKeyConstants.getLoginTokenKey(uuid);
            String str = stringRedisTemplate.opsForValue().get(userKey);
            return JSON.parseObject(str, LoginUser.class);
        }
        return null;
    }

    @Override
    public void refreshToken(LoginUser loginUser) {
        long currentTime = System.currentTimeMillis();
        if (loginUser.getExpireTime() - currentTime <= MILLIS_MINUTE_TEN) {
            set(loginUser);
        }
    }

    private void set(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        stringRedisTemplate.opsForValue().set(RedisKeyConstants.getLoginTokenKey(loginUser.getToken()), JSON.toJSONString(loginUser), expireTime, TimeUnit.MINUTES);
    }

}
