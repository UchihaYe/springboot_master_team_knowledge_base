package com.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("settings")
@Schema(description = "系统设置实体")
public class Setting {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "设置ID")
    private Long id;

    @Schema(description = "设置键")
    private String settingKey;

    @Schema(description = "设置值")
    private String settingValue;

    @Schema(description = "设置描述")
    private String description;

    @Schema(description = "设置类型")
    private String type;

    @Schema(description = "设置分组")
    private String group;

    @Schema(description = "是否系统设置")
    private Boolean isSystem;

    @Schema(description = "是否公开")
    private Boolean isPublic;

    @Schema(description = "更新者ID")
    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}