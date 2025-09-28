package com.teamkb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "space_user_groups")
public class SpaceUserGroup extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @Size(max = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @OneToMany(mappedBy = "userGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SpaceUserGroupMember> members = new HashSet<>();

    @OneToMany(mappedBy = "userGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DocumentPermission> documentPermissions = new HashSet<>();

    // Constructors
    public SpaceUserGroup() {}

    public SpaceUserGroup(String name, String description, Space space) {
        this.name = name;
        this.description = description;
        this.space = space;
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

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public Set<SpaceUserGroupMember> getMembers() {
        return members;
    }

    public void setMembers(Set<SpaceUserGroupMember> members) {
        this.members = members;
    }

    public Set<DocumentPermission> getDocumentPermissions() {
        return documentPermissions;
    }

    public void setDocumentPermissions(Set<DocumentPermission> documentPermissions) {
        this.documentPermissions = documentPermissions;
    }
}