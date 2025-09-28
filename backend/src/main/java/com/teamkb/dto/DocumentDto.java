package com.teamkb.dto;

import com.teamkb.entity.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentDto {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    private String content;
    private String contentPreview;
    private String path;
    private Document.DocumentType type;
    private Document.DocumentStatus status;
    private Integer version;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer reviewCycleDays;
    private LocalDateTime nextReviewDate;
    private LocalDateTime archivedAt;
    private Long spaceId;
    private String spaceName;
    private Long authorId;
    private String authorName;
    private Long parentId;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public DocumentDto() {}

    public DocumentDto(Document document) {
        this.id = document.getId();
        this.title = document.getTitle();
        this.content = document.getContent();
        this.contentPreview = document.getContentPreview();
        this.path = document.getPath();
        this.type = document.getType();
        this.status = document.getStatus();
        this.version = document.getVersion();
        this.viewCount = document.getViewCount();
        this.likeCount = document.getLikeCount();
        this.commentCount = document.getCommentCount();
        this.reviewCycleDays = document.getReviewCycleDays();
        this.nextReviewDate = document.getNextReviewDate();
        this.archivedAt = document.getArchivedAt();
        this.spaceId = document.getSpace().getId();
        this.spaceName = document.getSpace().getName();
        this.authorId = document.getAuthor().getId();
        this.authorName = document.getAuthor().getDisplayName();
        this.parentId = document.getParent() != null ? document.getParent().getId() : null;
        this.tags = document.getTags().stream()
            .map(dt -> dt.getTag().getName())
            .collect(Collectors.toList());
        this.createdAt = document.getCreatedAt();
        this.updatedAt = document.getUpdatedAt();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentPreview() {
        return contentPreview;
    }

    public void setContentPreview(String contentPreview) {
        this.contentPreview = contentPreview;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Document.DocumentType getType() {
        return type;
    }

    public void setType(Document.DocumentType type) {
        this.type = type;
    }

    public Document.DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(Document.DocumentStatus status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getReviewCycleDays() {
        return reviewCycleDays;
    }

    public void setReviewCycleDays(Integer reviewCycleDays) {
        this.reviewCycleDays = reviewCycleDays;
    }

    public LocalDateTime getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(LocalDateTime nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public LocalDateTime getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(LocalDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Static factory method
    public static DocumentDto fromEntity(Document document) {
        return new DocumentDto(document);
    }
}