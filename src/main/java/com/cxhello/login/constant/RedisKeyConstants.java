package com.cxhello.login.constant;

/**
 * @author cxhello
 * @date 2021/10/26
 */
public class RedisKeyConstants {

    private RedisKeyConstants() {

    }

    /**
     * 登录用户 redis key
     */
    private static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 注册邮箱key
     *
     */
    private static final String REGISTER_EMAIL_KEY = "register:email:";

    public static String getLoginTokenKey(String uuid) {
        return String.format("%s%s", LOGIN_TOKEN_KEY, uuid);
    }

    public static String getRegisterEmailKey(String email) {
        return String.format("%s%s", REGISTER_EMAIL_KEY, email);
    }

}
