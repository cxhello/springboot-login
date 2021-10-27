package com.cxhello.login.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cxhello.login.dto.UserDto;
import com.cxhello.login.entity.BaseEntity;
import com.cxhello.login.entity.User;
import com.cxhello.login.enums.LogicDeleteEnum;
import com.cxhello.login.exception.BusinessException;
import com.cxhello.login.service.UserService;
import com.cxhello.login.util.Result;
import com.cxhello.login.util.ResultUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

/**
 * @author cxhello
 * @date 2021/10/26
 */
@Validated
@RestController
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 注册
     * @param userDto
     * @return
     */
    @PostMapping(value = "/register")
    public Result<Object> register(@RequestBody @Valid UserDto userDto) {
        User user = userService.getOne(Wrappers.lambdaQuery(new User())
                .eq(User::getPhotoNumber, userDto.getPhotoNumber())
                .eq(BaseEntity::getIsDelete, LogicDeleteEnum.NOT_DELETED.getValue()));
        if (user != null) {
            throw new BusinessException("该手机号已存在");
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
