package com.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_group_members")
@Schema(description = "用户组成员实体")
public class UserGroupMember {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "成员ID")
    private Long id;

    @Schema(description = "用户组ID")
    private Long groupId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "角色")
    private String role;

    @Schema(description = "添加者ID")
    private Long addedBy;

    @Schema(description = "状态")
    private String status;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}