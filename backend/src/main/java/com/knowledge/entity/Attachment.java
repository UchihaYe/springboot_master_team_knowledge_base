package com.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("attachments")
@Schema(description = "附件实体")
public class Attachment {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "附件ID")
    private Long id;

    @Schema(description = "文件名")
    private String filename;

    @Schema(description = "原始文件名")
    private String originalName;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "MIME类型")
    private String mimeType;

    @Schema(description = "目标类型")
    private String targetType;

    @Schema(description = "目标ID")
    private Long targetId;

    @Schema(description = "上传者ID")
    private Long uploadedBy;

    @Schema(description = "下载次数")
    private Integer downloadCount;

    @Schema(description = "文件哈希")
    private String fileHash;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @TableLogic
    @Schema(description = "删除时间")
    private LocalDateTime deletedAt;
}