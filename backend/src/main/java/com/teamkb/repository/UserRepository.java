package com.teamkb.repository;

import com.teamkb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailVerificationToken(String token);

    Optional<User> findByPasswordResetToken(String token);

    @Query("SELECT u FROM User u WHERE u.passwordResetToken = :token AND u.passwordResetExpiresAt > :now")
    Optional<User> findByValidPasswordResetToken(@Param("token") String token, @Param("now") LocalDateTime now);

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL")
    List<User> findAllActive();

    @Query("SELECT u FROM User u WHERE u.enabled = true AND u.deletedAt IS NULL")
    List<User> findAllEnabledActive();

    @Query("SELECT u FROM User u WHERE u.role = 'SYSTEM_ADMIN' AND u.deletedAt IS NULL")
    List<User> findAllSystemAdmins();

    @Query("SELECT COUNT(u) FROM User u WHERE u.deletedAt IS NULL")
    long countActiveUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.lastLoginAt >= :since AND u.deletedAt IS NULL")
    long countActiveUsersSince(@Param("since") LocalDateTime since);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.displayName LIKE %:keyword% OR u.email LIKE %:keyword% AND u.deletedAt IS NULL")
    List<User> searchByKeyword(@Param("keyword") String keyword);
}