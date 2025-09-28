package com.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("document_tags")
@Schema(description = "文档标签关联实体")
public class DocumentTag {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "关联ID")
    private Long id;

    @Schema(description = "文档ID")
    private Long documentId;

    @Schema(description = "标签ID")
    private Long tagId;

    @Schema(description = "添加者ID")
    private Long addedBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}