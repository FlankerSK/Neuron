package com.practicum.neuron.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 后端响应状态
 */
@Data
@Builder
public class Status {
    // 错误码
    private int code;
    //错误信息
    private String message;

    public Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Status(Status status) {
        this.code = status.code;
        this.message = status.message;
    }
    /*      预定义的响应状态      */

    /*   0  请求成功   */
    public final static Status SUCCESS = new Status(0, "请求成功");

    /*  1XXX 登录错误  */
    public final static Status LOGIN_UNKNOWN_ERROR = new Status(1000, "未知登陆错误");
    public final static Status LOGIN_BAD_CREDENTIAL = new Status(1001, "用户名或密码错误");
    public final static Status LOGIN_TOO_MANY_TIMES = new Status(1002, "登录尝试次数过多");
    public final static Status LOGIN_WRONG_CAPTCHA = new Status(1003, "验证码错误");
    public final static Status LOGIN_ACCOUNT_EXPIRED = new Status(1004, "账号已过期");
    public final static Status LOGIN_ACCOUNT_LOCKED = new Status(1005, "账号已锁定");
    public final static Status LOGIN_ACCOUNT_DISABLED = new Status(1006, "账号不可用");

    /*  2XXX 注册错误  */
    public final static Status REGISTER_UNKNOWN_ERROR = new Status(2000, "未知注册错误");
    public final static Status REGISTER_USER_EXIST = new Status(2001, "用户名已存在");
    public final static Status REGISTER_USER_NOT_EXIST = new Status(2002, "用户名不存在");
    public final static Status REGISTER_SAME_PASSWORD = new Status(2003, "旧密码与新密码相同");

    /*  3XXX 访问错误  */
    public final static Status ACCESS_UNKNOWN_ERROR = new Status(3000, "未知访问错误");
    public final static Status ACCESS_NOT_FOUND = new Status(3001, "资源不存在");
    public final static Status ACCESS_METHOD_NOT_ALLOWED = new Status(3002, "请求方法不被允许");
    public final static Status ACCESS_NEED_LOGIN = new Status(3003, "需要登录");
    public final static Status ACCESS_NO_AUTHORITY = new Status(3004, "当前用户没有访问权限");
    public final static Status ACCESS_CREDENTIAL_EXPIRED = new Status(3005, "登录凭据已过期");
    public final static Status ACCESS_INVALID_TOKEN = new Status(3006, "无效的Token");
}
