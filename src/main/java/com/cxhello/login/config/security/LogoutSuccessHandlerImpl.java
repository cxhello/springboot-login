package com.cxhello.login.config.security;

import com.alibaba.fastjson.JSON;
import com.cxhello.login.entity.LoginUser;
import com.cxhello.login.service.UserService;
import com.cxhello.login.util.JwtUtils;
import com.cxhello.login.util.ResultUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author cxhello
 * @date 2021/10/26
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private UserService userService;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String token = jwtUtils.getToken(httpServletRequest);
        LoginUser loginUser = userService.getLoginUser(token);
        if (loginUser != null) {
            userService.logout(loginUser.getToken());
        }
        httpServletResponse.setStatus(200);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.getWriter().print(JSON.toJSONString(ResultUtils.result(HttpStatus.OK.value(), "退出成功")));
    }
}
