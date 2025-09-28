package com.teamkb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag extends BaseEntity {

    @NotBlank
    @Size(max = 50)
    @Column(unique = true, nullable = false)
    private String name;

    @Size(max = 200)
    private String description;

    @Column(name = "color")
    private String color;

    @Column(name = "usage_count", nullable = false)
    private Integer usageCount = 0;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DocumentTag> documentTags = new HashSet<>();

    // Constructors
    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

    public Tag(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public Set<DocumentTag> getDocumentTags() {
        return documentTags;
    }

    public void setDocumentTags(Set<DocumentTag> documentTags) {
        this.documentTags = documentTags;
    }

    // Helper methods
    public void incrementUsageCount() {
        this.usageCount++;
    }

    public void decrementUsageCount() {
        this.usageCount = Math.max(0, this.usageCount - 1);
    }
}