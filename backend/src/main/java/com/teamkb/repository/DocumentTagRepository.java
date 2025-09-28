package com.teamkb.repository;

import com.teamkb.entity.Document;
import com.teamkb.entity.DocumentTag;
import com.teamkb.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentTagRepository extends JpaRepository<DocumentTag, Long> {

    Optional<DocumentTag> findByDocumentAndTag(Document document, Tag tag);

    @Query("SELECT dt FROM DocumentTag dt WHERE dt.document = :document AND dt.deletedAt IS NULL")
    List<DocumentTag> findByDocument(@Param("document") Document document);

    @Query("SELECT dt FROM DocumentTag dt WHERE dt.tag = :tag AND dt.deletedAt IS NULL")
    List<DocumentTag> findByTag(@Param("tag") Tag tag);

    @Query("SELECT COUNT(dt) FROM DocumentTag dt WHERE dt.tag = :tag AND dt.deletedAt IS NULL")
    long countByTag(@Param("tag") Tag tag);

    boolean existsByDocumentAndTag(Document document, Tag tag);
}