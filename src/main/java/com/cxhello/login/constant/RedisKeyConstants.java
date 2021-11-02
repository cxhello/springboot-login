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
     * 用户验证码key
     */
    private static final String USER_CODE_KEY = "user:code:";

    public static String getLoginTokenKey(String uuid) {
        return String.format("%s%s", LOGIN_TOKEN_KEY, uuid);
    }

    public static String getUserCodeKey(String email) {
        return String.format("%s%s", USER_CODE_KEY, email);
    }

}
