package com.cxhello.login.enums;

/**
 * @author cxhello
 * @date 2021/10/26
 */
public enum LogicDeleteEnum {

    /**
     * 未删除
     */
    NOT_DELETED(0, "未删除");


    LogicDeleteEnum(long value, String name) {
        this.value = value;
        this.name = name;
    }

    private long value;

    private String name;

    public long getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
