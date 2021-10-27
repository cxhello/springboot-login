package com.cxhello.login.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author cxhello
 * @date 2021/10/26
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 5983556688124093812L;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 是否删除，0-未删除，其他值已删除
     */
    private Long isDelete;

}
