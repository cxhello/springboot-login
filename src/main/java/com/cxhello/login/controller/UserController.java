package com.cxhello.login.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cxhello.login.constant.RedisKeyConstants;
import com.cxhello.login.dto.UserDto;
import com.cxhello.login.entity.BaseEntity;
import com.cxhello.login.entity.User;
import com.cxhello.login.enums.LogicDeleteEnum;
import com.cxhello.login.exception.BusinessException;
import com.cxhello.login.service.UserService;
import com.cxhello.login.util.EmailUtils;
import com.cxhello.login.util.Result;
import com.cxhello.login.util.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author cxhello
 * @date 2021/10/26
 */
@Validated
@RestController
public class UserController {

    @Value("${email.expireTime}")
    private int expireTime;

    @Resource
    private UserService userService;

    @Resource
    private EmailUtils emailUtils;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "/sendEmailVerificationCode")
    public Result<Object> sendEmailVerificationCode(@Valid @NotBlank(message = "邮箱不能为空") @Email(message = "邮箱格式不合法") String email) {
        String emailVerificationCode = stringRedisTemplate.opsForValue().get(RedisKeyConstants.getUserCodeKey(email));
        if (StringUtils.isNotBlank(emailVerificationCode)) {
            throw new BusinessException("请稍后再试");
        }
        emailVerificationCode = emailUtils.send(email);
        stringRedisTemplate.opsForValue().set(RedisKeyConstants.getUserCodeKey(email), emailVerificationCode, expireTime, TimeUnit.MINUTES);
        return ResultUtils.success();
    }

    /**
     * 注册
     * @param userDto
     * @return
     */
    @PostMapping(value = "/register")
    public Result<Object> register(@RequestBody @Valid UserDto userDto) {
        User user = userService.getOne(Wrappers.lambdaQuery(new User())
                .eq(User::getEmail, userDto.getEmail())
                .eq(BaseEntity::getIsDelete, LogicDeleteEnum.NOT_DELETED.getValue()));
        if (user != null) {
            throw new BusinessException("该邮箱已存在");
        }
        String s = stringRedisTemplate.opsForValue().get(RedisKeyConstants.getUserCodeKey(userDto.getEmail()));
        if (!userDto.getEmailVerificationCode().equals(s)) {
            throw new BusinessException("验证码不正确");
        }
        user = new User();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        BeanUtils.copyProperties(userDto, user);
        user.setCreateTime(new Date());
        user.setCreateUser(user.getNickName());
        userService.save(user);
        return ResultUtils.success();
    }

    /**
     * 登录
     * @param userDto
     * @return
     */
    @PostMapping(value = "/login")
    public Result<String> login(@RequestBody @Validated(UserDto.Login.class) UserDto userDto) {
        String token = userService.login(userDto);
        return ResultUtils.result(token);
    }


}
