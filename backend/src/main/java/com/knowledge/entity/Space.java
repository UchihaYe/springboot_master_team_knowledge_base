package com.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("spaces")
@Schema(description = "知识空间实体")
public class Space {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "空间ID")
    private Long id;

    @Schema(description = "空间名称")
    private String name;

    @Schema(description = "空间描述")
    private String description;

    @Schema(description = "空间图标")
    private String icon;

    @Schema(description = "空间类型")
    private String type;

    @Schema(description = "空间状态")
    private String status;

    @Schema(description = "创建者ID")
    private Long createdBy;

    @Schema(description = "父级空间ID")
    private Long parentId;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "空间设置JSON")
    private String settings;

    @Schema(description = "是否私有")
    private Boolean isPrivate;

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