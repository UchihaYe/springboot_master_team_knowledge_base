package com.teamkb.repository;

import com.teamkb.entity.Document;
import com.teamkb.entity.DocumentVersion;
import com.teamkb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {

    @Query("SELECT dv FROM DocumentVersion dv WHERE dv.document = :document ORDER BY dv.versionNumber DESC")
    List<DocumentVersion> findByDocumentOrderByVersionDesc(@Param("document") Document document);

    @Query("SELECT dv FROM DocumentVersion dv WHERE dv.document = :document AND dv.versionNumber = :versionNumber")
    DocumentVersion findByDocumentAndVersionNumber(@Param("document") Document document, @Param("versionNumber") Integer versionNumber);

    @Query("SELECT dv FROM DocumentVersion dv WHERE dv.author = :author ORDER BY dv.createdAt DESC")
    List<DocumentVersion> findByAuthor(@Param("author") User author);

    @Query("SELECT COUNT(dv) FROM DocumentVersion dv WHERE dv.document = :document")
    long countByDocument(@Param("document") Document document);

    @Query("SELECT dv FROM DocumentVersion dv WHERE dv.document = :document ORDER BY dv.versionNumber ASC")
    List<DocumentVersion> findByDocumentOrderByVersionAsc(@Param("document") Document document);
}