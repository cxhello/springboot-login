package com.cxhello.login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxhello.login.dto.UserDto;
import com.cxhello.login.entity.LoginUser;
import com.cxhello.login.entity.User;

/**
 * @author cxhello
 * @date 2021/10/26
 */
public interface UserService extends IService<User> {

    /**
     * 登录
     * @param userDto
     * @return
     */
    String login(UserDto userDto);

    /**
     * 登出
     * @param token
     */
    void logout(String token);

    /**
     * 通过token获取用户信息
     * @param token
     * @return
     */
    LoginUser getLoginUser(String token);

    /**
     * 刷新令牌有效期
     */
    void refreshToken(LoginUser loginUser);

}
