package com.cxhello.login.controller;

import com.cxhello.login.util.Result;
import com.cxhello.login.util.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cxhello
 * @date 2021/10/26
 */
@RestController
public class TestController {

    /**
     * 测试登录成功访问
     * @return
     */
    @RequestMapping(value = "/test")
    public Result<String> test() {
        return ResultUtils.result("测试登录成功访问");
    }

}
