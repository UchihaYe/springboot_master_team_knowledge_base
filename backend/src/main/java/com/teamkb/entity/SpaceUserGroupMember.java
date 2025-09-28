package com.teamkb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "space_user_group_members", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_group_id", "user_id"})
})
public class SpaceUserGroupMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id", nullable = false)
    private SpaceUserGroup userGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public SpaceUserGroupMember() {}

    public SpaceUserGroupMember(SpaceUserGroup userGroup, User user) {
        this.userGroup = userGroup;
        this.user = user;
    }

    // Getters and Setters
    public SpaceUserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(SpaceUserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}