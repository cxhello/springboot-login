package com.cxhello.login.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cxhello.login.entity.BaseEntity;
import com.cxhello.login.entity.LoginUser;
import com.cxhello.login.entity.User;
import com.cxhello.login.enums.LogicDeleteEnum;
import com.cxhello.login.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author cxhello
 * @date 2021/10/26
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userService.getOne(Wrappers.lambdaQuery(new User())
                .eq(User::getPhotoNumber, phoneNumber)
                .eq(BaseEntity::getIsDelete, LogicDeleteEnum.NOT_DELETED.getValue()));
        if (user == null) {
            throw new UsernameNotFoundException("登录手机号：" + phoneNumber + " 不存在");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        return loginUser;
    }

}
