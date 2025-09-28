package com.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("document_versions")
@Schema(description = "文档版本实体")
public class DocumentVersion {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "版本ID")
    private Long id;

    @Schema(description = "文档ID")
    private Long documentId;

    @Schema(description = "版本号")
    private Integer version;

    @Schema(description = "版本标题")
    private String title;

    @Schema(description = "版本内容")
    private String content;

    @Schema(description = "版本摘要")
    private String summary;

    @Schema(description = "变更说明")
    private String changeLog;

    @Schema(description = "变更类型")
    private String changeType;

    @Schema(description = "创建者ID")
    private Long createdBy;

    @Schema(description = "内容大小")
    private Long contentSize;

    @Schema(description = "是否为主版本")
    private Boolean isMajor;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}