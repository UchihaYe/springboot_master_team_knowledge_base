package com.teamkb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "document_tags", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"document_id", "tag_id"})
})
public class DocumentTag extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    // Constructors
    public DocumentTag() {}

    public DocumentTag(Document document, Tag tag) {
        this.document = document;
        this.tag = tag;
    }

    // Getters and Setters
    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}