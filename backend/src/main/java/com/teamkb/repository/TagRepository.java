package com.teamkb.repository;

import com.teamkb.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Query("SELECT t FROM Tag t WHERE t.deletedAt IS NULL ORDER BY t.usageCount DESC")
    List<Tag> findAllOrderByUsageDesc();

    @Query("SELECT t FROM Tag t WHERE t.name LIKE %:keyword% AND t.deletedAt IS NULL")
    List<Tag> findByNameContaining(@Param("keyword") String keyword);

    @Query("SELECT t FROM Tag t WHERE t.usageCount > 0 AND t.deletedAt IS NULL ORDER BY t.usageCount DESC")
    List<Tag> findPopularTags();

    @Query("SELECT t FROM Tag t WHERE t.usageCount = 0 AND t.deletedAt IS NULL")
    List<Tag> findUnusedTags();

    boolean existsByName(String name);

    @Query("SELECT COUNT(t) FROM Tag t WHERE t.deletedAt IS NULL")
    long countActiveTags();
}