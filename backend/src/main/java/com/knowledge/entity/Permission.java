package com.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("permissions")
@Schema(description = "权限实体")
public class Permission {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "权限ID")
    private Long id;

    @Schema(description = "资源类型")
    private String resourceType;

    @Schema(description = "资源ID")
    private Long resourceId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户组ID")
    private Long groupId;

    @Schema(description = "权限类型")
    private String permissionType;

    @Schema(description = "授权者ID")
    private Long grantedBy;

    @Schema(description = "过期时间")
    private LocalDateTime expiresAt;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}