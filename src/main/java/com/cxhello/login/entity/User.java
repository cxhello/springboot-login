package com.cxhello.login.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author cxhello
 * @date 2021/10/26
 */
@Data
public class User extends BaseEntity {

    private static final long serialVersionUID = 3194049329226726004L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

}
