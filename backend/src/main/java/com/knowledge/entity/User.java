package com.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("users")
@Schema(description = "User Entity")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "User ID")
    private Long id;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Password Hash")
    private String passwordHash;

    @Schema(description = "Display Name")
    private String displayName;

    @Schema(description = "Avatar URL")
    private String avatarUrl;

    @Schema(description = "Is System Admin")
    private Boolean isSystemAdmin;

    @Schema(description = "Status")
    private String status;

    @Schema(description = "Last Login Time")
    private LocalDateTime lastLoginAt;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Created Time")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "Updated Time")
    private LocalDateTime updatedAt;
}