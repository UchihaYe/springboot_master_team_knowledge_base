package com.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("documents")
@Schema(description = "文档实体")
public class Document {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "文档ID")
    private Long id;

    @Schema(description = "文档标题")
    private String title;

    @Schema(description = "文档内容")
    private String content;

    @Schema(description = "文档摘要")
    private String summary;

    @Schema(description = "文档类型")
    private String type;

    @Schema(description = "文档状态")
    private String status;

    @Schema(description = "文档标签")
    private String tags;

    @Schema(description = "空间ID")
    private Long spaceId;

    @Schema(description = "创建者ID")
    private Long createdBy;

    @Schema(description = "更新者ID")
    private Long updatedBy;

    @Schema(description = "父级文档ID")
    private Long parentId;

    @Schema(description = "版本号")
    private Integer version;

    @Schema(description = "是否锁定")
    private Boolean isLocked;

    @Schema(description = "锁定者ID")
    private Long lockedBy;

    @Schema(description = "锁定时间")
    private LocalDateTime lockedAt;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "访问级别")
    private String accessLevel;

    @Schema(description = "查看次数")
    private Integer viewCount;

    @Schema(description = "点赞次数")
    private Integer likeCount;

    @Schema(description = "收藏次数")
    private Integer favoriteCount;

    @Schema(description = "评论次数")
    private Integer commentCount;

    @Schema(description = "发布时间")
    private LocalDateTime publishedAt;

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