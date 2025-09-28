package com.teamkb.repository;

import com.teamkb.entity.Document;
import com.teamkb.entity.Space;
import com.teamkb.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT d FROM Document d WHERE d.deletedAt IS NULL")
    List<Document> findAllActive();

    @Query("SELECT d FROM Document d WHERE d.space = :space AND d.deletedAt IS NULL")
    List<Document> findBySpace(@Param("space") Space space);

    @Query("SELECT d FROM Document d WHERE d.space = :space AND d.archivedAt IS NULL AND d.deletedAt IS NULL")
    List<Document> findActiveBySpace(@Param("space") Space space);

    @Query("SELECT d FROM Document d WHERE d.space = :space AND d.parent IS NULL AND d.deletedAt IS NULL")
    List<Document> findRootDocumentsBySpace(@Param("space") Space space);

    @Query("SELECT d FROM Document d WHERE d.parent = :parent AND d.deletedAt IS NULL")
    List<Document> findByParent(@Param("parent") Document parent);

    @Query("SELECT d FROM Document d WHERE d.author = :author AND d.deletedAt IS NULL")
    List<Document> findByAuthor(@Param("author") User author);

    @Query("SELECT d FROM Document d WHERE d.author = :author AND d.updatedAt >= :since AND d.deletedAt IS NULL ORDER BY d.updatedAt DESC")
    List<Document> findRecentlyEditedByAuthor(@Param("author") User author, @Param("since") LocalDateTime since, Pageable pageable);

    @Query("SELECT DISTINCT d FROM Document d JOIN d.bookmarks b WHERE b.user = :user AND d.deletedAt IS NULL ORDER BY b.createdAt DESC")
    List<Document> findBookmarkedByUser(@Param("user") User user);

    @Query("SELECT d FROM Document d WHERE d.title LIKE %:keyword% OR d.content LIKE %:keyword% OR d.contentPreview LIKE %:keyword% AND d.deletedAt IS NULL AND d.archivedAt IS NULL")
    Page<Document> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT d FROM Document d JOIN d.tags dt JOIN dt.tag t WHERE t.name = :tagName AND d.deletedAt IS NULL AND d.archivedAt IS NULL")
    List<Document> findByTagName(@Param("tagName") String tagName);

    @Query("SELECT d FROM Document d WHERE d.nextReviewDate <= :date AND d.deletedAt IS NULL AND d.archivedAt IS NULL")
    List<Document> findDocumentsNeedingReview(@Param("date") LocalDateTime date);

    @Query("SELECT d FROM Document d WHERE d.archivedAt IS NOT NULL AND d.deletedAt IS NULL")
    List<Document> findAllArchived();

    @Query("SELECT d FROM Document d WHERE d.deletedAt IS NOT NULL")
    List<Document> findAllDeleted();

    @Query("SELECT COUNT(d) FROM Document d WHERE d.space = :space AND d.deletedAt IS NULL")
    long countBySpace(@Param("space") Space space);

    @Query("SELECT COUNT(d) FROM Document d WHERE d.author = :author AND d.deletedAt IS NULL")
    long countByAuthor(@Param("author") User author);

    @Query("SELECT COUNT(d) FROM Document d WHERE d.deletedAt IS NULL")
    long countActiveDocuments();

    @Query("SELECT d FROM Document d WHERE d.path = :path AND d.space = :space AND d.deletedAt IS NULL")
    Optional<Document> findByPathAndSpace(@Param("path") String path, @Param("space") Space space);

    Optional<Document> findByIdAndDeletedAtIsNull(Long id);

    @Query("SELECT d FROM Document d WHERE d.viewCount > 0 AND d.deletedAt IS NULL ORDER BY d.viewCount DESC")
    List<Document> findMostViewed(Pageable pageable);

    @Query("SELECT d FROM Document d WHERE d.likeCount > 0 AND d.deletedAt IS NULL ORDER BY d.likeCount DESC")
    List<Document> findMostLiked(Pageable pageable);

    @Query("SELECT d FROM Document d WHERE d.updatedAt < :date AND d.deletedAt IS NULL AND d.archivedAt IS NULL")
    List<Document> findStaleDocuments(@Param("date") LocalDateTime date);
}