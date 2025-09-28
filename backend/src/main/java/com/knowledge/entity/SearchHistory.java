package com.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("search_history")
@Schema(description = "搜索历史实体")
public class SearchHistory {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "搜索历史ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "搜索关键词")
    private String keyword;

    @Schema(description = "搜索类型")
    private String searchType;

    @Schema(description = "搜索结果数量")
    private Integer resultCount;

    @Schema(description = "搜索过滤条件")
    private String filters;

    @Schema(description = "IP地址")
    private String ipAddress;

    @Schema(description = "用户代理")
    private String userAgent;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}