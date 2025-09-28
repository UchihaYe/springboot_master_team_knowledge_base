package com.teamkb.repository;

import com.teamkb.entity.Comment;
import com.teamkb.entity.Document;
import com.teamkb.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.document = :document AND c.parent IS NULL AND c.deletedAt IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findRootCommentsByDocument(@Param("document") Document document);

    @Query("SELECT c FROM Comment c WHERE c.document = :document AND c.deletedAt IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findByDocument(@Param("document") Document document);

    @Query("SELECT c FROM Comment c WHERE c.parent = :parent AND c.deletedAt IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findRepliesByParent(@Param("parent") Comment parent);

    @Query("SELECT c FROM Comment c WHERE c.author = :author AND c.deletedAt IS NULL ORDER BY c.createdAt DESC")
    List<Comment> findByAuthor(@Param("author") User author);

    @Query("SELECT c FROM Comment c WHERE c.document = :document AND c.deletedAt IS NULL")
    Page<Comment> findByDocumentPaged(@Param("document") Document document, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.document = :document AND c.deletedAt IS NULL")
    long countByDocument(@Param("document") Document document);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.author = :author AND c.deletedAt IS NULL")
    long countByAuthor(@Param("author") User author);

    @Query("SELECT c FROM Comment c WHERE c.content LIKE %:keyword% AND c.deletedAt IS NULL")
    List<Comment> searchByContent(@Param("keyword") String keyword);

    @Query("SELECT c FROM Comment c WHERE c.deletedAt IS NULL ORDER BY c.likeCount DESC")
    List<Comment> findMostLiked(Pageable pageable);
}