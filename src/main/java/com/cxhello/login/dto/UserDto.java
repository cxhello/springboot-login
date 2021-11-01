package com.cxhello.login.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.*;
import javax.validation.groups.Default;

/**
 * @author cxhello
 * @date 2021/10/26
 */
@Data
public class UserDto {

    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickName;

    /**
     * 用户邮箱
     */
    @NotBlank(message = "邮箱不能为空", groups = {Default.class, Login.class})
    @Email(message = "邮箱格式不合法", groups = {Default.class, Login.class})
    private String email;

    /**
     * 邮箱验证码
     */
    @NotBlank(message = "邮箱验证码不能为空")
    private String emailVerificationCode;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {Default.class, Login.class})
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    public interface Login {

    }

    @AssertTrue(message = "两次密码输入不一致")
    private boolean isValid() {
        if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(confirmPassword)) {
            return password.equals(confirmPassword);
        }
        return true;
    }

}
