package com.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("activities")
@Schema(description = "活动记录实体")
public class Activity {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "活动ID")
    private Long id;

    @Schema(description = "活动类型")
    private String type;

    @Schema(description = "活动描述")
    private String description;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "目标类型")
    private String targetType;

    @Schema(description = "目标ID")
    private Long targetId;

    @Schema(description = "操作前数据")
    private String beforeData;

    @Schema(description = "操作后数据")
    private String afterData;

    @Schema(description = "IP地址")
    private String ipAddress;

    @Schema(description = "用户代理")
    private String userAgent;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}