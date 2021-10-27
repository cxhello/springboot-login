package com.cxhello.login.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import javax.validation.groups.Default;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * 用户性别，0-男，1-女，2-未知
     */
    @NotNull(message = "性别不能为空")
    @Range(min = 0, max = 2, message = "性别非法")
    private Integer sex;

    /**
     * 手机号码
     */
    @NotBlank(message = "手机号码不能为空", groups = {Default.class, Login.class})
    @Length(groups = {Default.class, Login.class})
    private String photoNumber;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {Default.class, Login.class})
    private String password;

    /**
     * 用户邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不合法")
    private String email;

    @AssertTrue(message = "手机号码非法", groups = {Default.class, Login.class})
    private boolean isValid() {
        if (StringUtils.isNotBlank(photoNumber)) {
            String pattern = "(13\\d|14[579]|15[^4\\D]|17[^49\\D]|18\\d)\\d{8}";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(photoNumber);
            return m.matches();
        }
        return true;
    }

    public interface Login {

    }

}
