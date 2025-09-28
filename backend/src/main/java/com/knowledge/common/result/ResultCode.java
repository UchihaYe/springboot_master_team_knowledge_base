package com.knowledge.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),

    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "方法不允许"),
    CONFLICT(409, "数据冲突"),

    INTERNAL_SERVER_ERROR(500, "系统内部错误"),

    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    INVALID_CREDENTIALS(1003, "用户名或密码错误"),
    ACCOUNT_LOCKED(1004, "账户已锁定"),
    EMAIL_NOT_VERIFIED(1005, "邮箱未验证"),
    PASSWORD_TOO_WEAK(1006, "密码强度不够"),
    LOGIN_ATTEMPTS_EXCEEDED(1007, "登录尝试次数过多"),

    SPACE_NOT_FOUND(2001, "知识空间不存在"),
    SPACE_ACCESS_DENIED(2002, "无权限访问该空间"),
    SPACE_NAME_EXISTS(2003, "空间名称已存在"),

    DOCUMENT_NOT_FOUND(3001, "文档不存在"),
    DOCUMENT_ACCESS_DENIED(3002, "无权限访问该文档"),
    DOCUMENT_LOCKED(3003, "文档已被锁定"),
    DOCUMENT_VERSION_CONFLICT(3004, "文档版本冲突"),

    COMMENT_NOT_FOUND(4001, "评论不存在"),
    COMMENT_ACCESS_DENIED(4002, "无权限操作该评论"),

    FILE_UPLOAD_FAILED(5001, "文件上传失败"),
    FILE_TYPE_NOT_ALLOWED(5002, "文件类型不允许"),
    FILE_SIZE_EXCEEDED(5003, "文件大小超出限制"),
    FILE_NOT_FOUND(5004, "文件不存在"),

    PERMISSION_DENIED(6001, "权限不足"),
    INVALID_PERMISSION_TYPE(6002, "权限类型无效"),

    TAG_NOT_FOUND(7001, "标签不存在"),
    TAG_NAME_EXISTS(7002, "标签名称已存在"),

    NOTIFICATION_NOT_FOUND(8001, "通知不存在"),
    NOTIFICATION_ACCESS_DENIED(8002, "无权限访问该通知"),

    SEARCH_QUERY_INVALID(9001, "搜索关键词无效"),
    SEARCH_RESULTS_EMPTY(9002, "搜索结果为空"),

    VALIDATION_FAILED(10001, "数据验证失败"),
    CAPTCHA_INVALID(10002, "验证码无效"),
    TOKEN_INVALID(10003, "令牌无效"),
    TOKEN_EXPIRED(10004, "令牌已过期"),

    SYSTEM_MAINTENANCE(11001, "系统维护中"),
    RATE_LIMIT_EXCEEDED(11002, "请求频率超限"),

    DATABASE_ERROR(12001, "数据库错误"),
    CACHE_ERROR(12002, "缓存错误"),
    EXTERNAL_API_ERROR(12003, "外部API错误");

    private final Integer code;
    private final String message;
}