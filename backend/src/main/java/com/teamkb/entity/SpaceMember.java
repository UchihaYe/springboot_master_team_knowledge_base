package com.teamkb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "space_members")
public class SpaceMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private SpaceRole role;

    public enum SpaceRole {
        SPACE_ADMIN, EDITOR, COMMENTER, VIEWER
    }

    // Constructors
    public SpaceMember() {}

    public SpaceMember(Space space, User user, SpaceRole role) {
        this.space = space;
        this.user = user;
        this.role = role;
    }

    // Getters and Setters
    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SpaceRole getRole() {
        return role;
    }

    public void setRole(SpaceRole role) {
        this.role = role;
    }

    // Helper methods
    public boolean isSpaceAdmin() {
        return role == SpaceRole.SPACE_ADMIN;
    }

    public boolean canEdit() {
        return role == SpaceRole.SPACE_ADMIN || role == SpaceRole.EDITOR;
    }

    public boolean canComment() {
        return role == SpaceRole.SPACE_ADMIN || role == SpaceRole.EDITOR || role == SpaceRole.COMMENTER;
    }

    public boolean canView() {
        return true; // All space members can view
    }
}