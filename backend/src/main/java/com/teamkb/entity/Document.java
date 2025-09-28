package com.teamkb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "documents")
public class Document extends BaseEntity {

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "content_preview", length = 500)
    private String contentPreview;

    @Column(name = "path", nullable = false)
    private String path;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DocumentType type = DocumentType.DOCUMENT;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DocumentStatus status = DocumentStatus.DRAFT;

    @Column(name = "version", nullable = false)
    private Integer version = 1;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0;

    @Column(name = "comment_count", nullable = false)
    private Integer commentCount = 0;

    @Column(name = "review_cycle_days")
    private Integer reviewCycleDays;

    @Column(name = "next_review_date")
    private LocalDateTime nextReviewDate;

    @Column(name = "archived_at")
    private LocalDateTime archivedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Document parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Document> children = new HashSet<>();

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DocumentVersion> versions = new HashSet<>();

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DocumentTag> tags = new HashSet<>();

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DocumentLike> likes = new HashSet<>();

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DocumentBookmark> bookmarks = new HashSet<>();

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DocumentPermission> permissions = new HashSet<>();

    public enum DocumentType {
        DOCUMENT, FOLDER
    }

    public enum DocumentStatus {
        DRAFT, PUBLISHED, ARCHIVED
    }

    // Constructors
    public Document() {}

    public Document(String title, String content, String path, Space space, User author) {
        this.title = title;
        this.content = content;
        this.path = path;
        this.space = space;
        this.author = author;
    }

    // Getters and Setters
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
        // Update preview
        if (content != null && content.length() > 500) {
            this.contentPreview = content.substring(0, 500) + "...";
        } else {
            this.contentPreview = content;
        }
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

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
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

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Document getParent() {
        return parent;
    }

    public void setParent(Document parent) {
        this.parent = parent;
    }

    public Set<Document> getChildren() {
        return children;
    }

    public void setChildren(Set<Document> children) {
        this.children = children;
    }

    public Set<DocumentVersion> getVersions() {
        return versions;
    }

    public void setVersions(Set<DocumentVersion> versions) {
        this.versions = versions;
    }

    public Set<DocumentTag> getTags() {
        return tags;
    }

    public void setTags(Set<DocumentTag> tags) {
        this.tags = tags;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<DocumentLike> getLikes() {
        return likes;
    }

    public void setLikes(Set<DocumentLike> likes) {
        this.likes = likes;
    }

    public Set<DocumentBookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(Set<DocumentBookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public Set<DocumentPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<DocumentPermission> permissions) {
        this.permissions = permissions;
    }

    // Helper methods
    public boolean isFolder() {
        return type == DocumentType.FOLDER;
    }

    public boolean isDocument() {
        return type == DocumentType.DOCUMENT;
    }

    public boolean isDraft() {
        return status == DocumentStatus.DRAFT;
    }

    public boolean isPublished() {
        return status == DocumentStatus.PUBLISHED;
    }

    public boolean isArchived() {
        return archivedAt != null;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void incrementVersion() {
        this.version++;
    }

    public void archive() {
        this.archivedAt = LocalDateTime.now();
        this.status = DocumentStatus.ARCHIVED;
    }

    public void unarchive() {
        this.archivedAt = null;
        this.status = DocumentStatus.PUBLISHED;
    }

    public void publish() {
        this.status = DocumentStatus.PUBLISHED;
    }

    public void calculateNextReviewDate() {
        if (reviewCycleDays != null && reviewCycleDays > 0) {
            this.nextReviewDate = LocalDateTime.now().plusDays(reviewCycleDays);
        }
    }
}