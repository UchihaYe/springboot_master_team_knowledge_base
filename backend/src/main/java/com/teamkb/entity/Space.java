package com.teamkb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "spaces")
public class Space extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @Size(max = 500)
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "icon_url")
    private String iconUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SpaceStatus status = SpaceStatus.ACTIVE;

    @Column(name = "public_sharing_enabled", nullable = false)
    private Boolean publicSharingEnabled = false;

    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SpaceMember> members = new HashSet<>();

    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Document> documents = new HashSet<>();

    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SpaceUserGroup> userGroups = new HashSet<>();

    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DocumentTemplate> templates = new HashSet<>();

    public enum SpaceStatus {
        ACTIVE, ARCHIVED
    }

    // Constructors
    public Space() {}

    public Space(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public SpaceStatus getStatus() {
        return status;
    }

    public void setStatus(SpaceStatus status) {
        this.status = status;
    }

    public Boolean getPublicSharingEnabled() {
        return publicSharingEnabled;
    }

    public void setPublicSharingEnabled(Boolean publicSharingEnabled) {
        this.publicSharingEnabled = publicSharingEnabled;
    }

    public Set<SpaceMember> getMembers() {
        return members;
    }

    public void setMembers(Set<SpaceMember> members) {
        this.members = members;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public Set<SpaceUserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Set<SpaceUserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public Set<DocumentTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(Set<DocumentTemplate> templates) {
        this.templates = templates;
    }

    // Helper methods
    public boolean isActive() {
        return status == SpaceStatus.ACTIVE;
    }

    public boolean isArchived() {
        return status == SpaceStatus.ARCHIVED;
    }

    public void archive() {
        this.status = SpaceStatus.ARCHIVED;
    }

    public void activate() {
        this.status = SpaceStatus.ACTIVE;
    }
}