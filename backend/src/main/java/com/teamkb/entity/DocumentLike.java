package com.teamkb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "document_likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"document_id", "user_id"})
})
public class DocumentLike extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public DocumentLike() {}

    public DocumentLike(Document document, User user) {
        this.document = document;
        this.user = user;
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
}