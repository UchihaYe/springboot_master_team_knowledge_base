package com.teamkb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "document_permissions")
public class DocumentPermission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id")
    private SpaceUserGroup userGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false)
    private PermissionType permission;

    @Column(name = "inherit_from_parent", nullable = false)
    private Boolean inheritFromParent = true;

    public enum PermissionType {
        VIEW, COMMENT, EDIT
    }

    // Constructors
    public DocumentPermission() {}

    public DocumentPermission(Document document, User user, PermissionType permission) {
        this.document = document;
        this.user = user;
        this.permission = permission;
    }

    public DocumentPermission(Document document, SpaceUserGroup userGroup, PermissionType permission) {
        this.document = document;
        this.userGroup = userGroup;
        this.permission = permission;
    }

    // Getters and Setters
    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SpaceUserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(SpaceUserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public PermissionType getPermission() {
        return permission;
    }

    public void setPermission(PermissionType permission) {
        this.permission = permission;
    }

    public Boolean getInheritFromParent() {
        return inheritFromParent;
    }

    public void setInheritFromParent(Boolean inheritFromParent) {
        this.inheritFromParent = inheritFromParent;
    }

    // Helper methods
    public boolean isUserPermission() {
        return user != null;
    }

    public boolean isGroupPermission() {
        return userGroup != null;
    }
}