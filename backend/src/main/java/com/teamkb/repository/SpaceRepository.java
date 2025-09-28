package com.teamkb.repository;

import com.teamkb.entity.Space;
import com.teamkb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {

    @Query("SELECT s FROM Space s WHERE s.deletedAt IS NULL")
    List<Space> findAllActive();

    @Query("SELECT s FROM Space s WHERE s.status = 'ACTIVE' AND s.deletedAt IS NULL")
    List<Space> findAllActiveAndNotArchived();

    @Query("SELECT s FROM Space s WHERE s.status = 'ARCHIVED' AND s.deletedAt IS NULL")
    List<Space> findAllArchived();

    @Query("SELECT s FROM Space s JOIN s.members sm WHERE sm.user = :user AND s.deletedAt IS NULL")
    List<Space> findSpacesByUser(@Param("user") User user);

    @Query("SELECT s FROM Space s JOIN s.members sm WHERE sm.user = :user AND s.status = 'ACTIVE' AND s.deletedAt IS NULL")
    List<Space> findActiveSpacesByUser(@Param("user") User user);

    @Query("SELECT s FROM Space s WHERE s.name LIKE %:keyword% AND s.deletedAt IS NULL")
    List<Space> findByNameContaining(@Param("keyword") String keyword);

    @Query("SELECT COUNT(s) FROM Space s WHERE s.deletedAt IS NULL")
    long countActiveSpaces();

    @Query("SELECT COUNT(s) FROM Space s WHERE s.status = 'ACTIVE' AND s.deletedAt IS NULL")
    long countActiveAndNotArchivedSpaces();

    Optional<Space> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByNameAndDeletedAtIsNull(String name);
}