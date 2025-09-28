package com.teamkb.repository;

import com.teamkb.entity.Space;
import com.teamkb.entity.SpaceMember;
import com.teamkb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpaceMemberRepository extends JpaRepository<SpaceMember, Long> {

    Optional<SpaceMember> findBySpaceAndUser(Space space, User user);

    @Query("SELECT sm FROM SpaceMember sm WHERE sm.space = :space AND sm.deletedAt IS NULL")
    List<SpaceMember> findBySpace(@Param("space") Space space);

    @Query("SELECT sm FROM SpaceMember sm WHERE sm.user = :user AND sm.deletedAt IS NULL")
    List<SpaceMember> findByUser(@Param("user") User user);

    @Query("SELECT sm FROM SpaceMember sm WHERE sm.space = :space AND sm.role = 'SPACE_ADMIN' AND sm.deletedAt IS NULL")
    List<SpaceMember> findSpaceAdminsBySpace(@Param("space") Space space);

    @Query("SELECT sm FROM SpaceMember sm WHERE sm.space = :space AND sm.role IN ('SPACE_ADMIN', 'EDITOR') AND sm.deletedAt IS NULL")
    List<SpaceMember> findEditorsAndAdminsBySpace(@Param("space") Space space);

    @Query("SELECT COUNT(sm) FROM SpaceMember sm WHERE sm.space = :space AND sm.deletedAt IS NULL")
    long countBySpace(@Param("space") Space space);

    boolean existsBySpaceAndUser(Space space, User user);

    @Query("SELECT sm FROM SpaceMember sm WHERE sm.space.id = :spaceId AND sm.user.id = :userId AND sm.deletedAt IS NULL")
    Optional<SpaceMember> findBySpaceIdAndUserId(@Param("spaceId") Long spaceId, @Param("userId") Long userId);
}