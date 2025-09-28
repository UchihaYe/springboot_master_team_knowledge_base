package com.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_groups")
@Schema(description = "用户组实体")
public class UserGroup {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "用户组ID")
    private Long id;

    @Schema(description = "组名")
    private String name;

    @Schema(description = "组描述")
    private String description;

    @Schema(description = "组类型")
    private String type;

    @Schema(description = "创建者ID")
    private Long createdBy;

    @Schema(description = "父级组ID")
    private Long parentId;

    @Schema(description = "组状态")
    private String status;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @TableLogic
    @Schema(description = "删除时间")
    private LocalDateTime deletedAt;
}