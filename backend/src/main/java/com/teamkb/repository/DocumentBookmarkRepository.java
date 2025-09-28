package com.teamkb.repository;

import com.teamkb.entity.Document;
import com.teamkb.entity.DocumentBookmark;
import com.teamkb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentBookmarkRepository extends JpaRepository<DocumentBookmark, Long> {

    Optional<DocumentBookmark> findByDocumentAndUser(Document document, User user);

    @Query("SELECT db FROM DocumentBookmark db WHERE db.user = :user AND db.deletedAt IS NULL ORDER BY db.createdAt DESC")
    List<DocumentBookmark> findByUser(@Param("user") User user);

    @Query("SELECT db.document FROM DocumentBookmark db WHERE db.user = :user AND db.deletedAt IS NULL ORDER BY db.createdAt DESC")
    List<Document> findBookmarkedDocumentsByUser(@Param("user") User user);

    @Query("SELECT COUNT(db) FROM DocumentBookmark db WHERE db.document = :document AND db.deletedAt IS NULL")
    long countByDocument(@Param("document") Document document);

    boolean existsByDocumentAndUser(Document document, User user);
}