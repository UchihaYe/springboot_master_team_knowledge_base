package com.teamkb.repository;

import com.teamkb.entity.Document;
import com.teamkb.entity.DocumentLike;
import com.teamkb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentLikeRepository extends JpaRepository<DocumentLike, Long> {

    Optional<DocumentLike> findByDocumentAndUser(Document document, User user);

    @Query("SELECT dl FROM DocumentLike dl WHERE dl.document = :document AND dl.deletedAt IS NULL")
    List<DocumentLike> findByDocument(@Param("document") Document document);

    @Query("SELECT dl FROM DocumentLike dl WHERE dl.user = :user AND dl.deletedAt IS NULL")
    List<DocumentLike> findByUser(@Param("user") User user);

    @Query("SELECT COUNT(dl) FROM DocumentLike dl WHERE dl.document = :document AND dl.deletedAt IS NULL")
    long countByDocument(@Param("document") Document document);

    boolean existsByDocumentAndUser(Document document, User user);

    @Query("SELECT dl.document FROM DocumentLike dl WHERE dl.user = :user AND dl.deletedAt IS NULL ORDER BY dl.createdAt DESC")
    List<Document> findLikedDocumentsByUser(@Param("user") User user);
}