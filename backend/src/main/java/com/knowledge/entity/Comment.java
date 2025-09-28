package com.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("comments")
@Schema(description = "评论实体")
public class Comment {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "评论ID")
    private Long id;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "目标类型")
    private String targetType;

    @Schema(description = "目标ID")
    private Long targetId;

    @Schema(description = "创建者ID")
    private Long createdBy;

    @Schema(description = "父级评论ID")
    private Long parentId;

    @Schema(description = "回复次数")
    private Integer replyCount;

    @Schema(description = "点赞次数")
    private Integer likeCount;

    @Schema(description = "评论状态")
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