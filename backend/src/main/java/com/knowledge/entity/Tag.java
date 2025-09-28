package com.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tags")
@Schema(description = "标签实体")
public class Tag {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "标签ID")
    private Long id;

    @Schema(description = "标签名称")
    private String name;

    @Schema(description = "标签颜色")
    private String color;

    @Schema(description = "标签描述")
    private String description;

    @Schema(description = "创建者ID")
    private Long createdBy;

    @Schema(description = "使用次数")
    private Integer useCount;

    @Schema(description = "标签类型")
    private String type;

    @Schema(description = "标签状态")
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