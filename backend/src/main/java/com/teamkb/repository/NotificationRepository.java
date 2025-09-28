package com.teamkb.repository;

import com.teamkb.entity.Notification;
import com.teamkb.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.recipient = :user AND n.deletedAt IS NULL ORDER BY n.createdAt DESC")
    List<Notification> findByRecipient(@Param("user") User user);

    @Query("SELECT n FROM Notification n WHERE n.recipient = :user AND n.deletedAt IS NULL ORDER BY n.createdAt DESC")
    Page<Notification> findByRecipientPaged(@Param("user") User user, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.recipient = :user AND n.isRead = false AND n.deletedAt IS NULL ORDER BY n.createdAt DESC")
    List<Notification> findUnreadByRecipient(@Param("user") User user);

    @Query("SELECT n FROM Notification n WHERE n.recipient = :user AND n.isRead = true AND n.deletedAt IS NULL ORDER BY n.createdAt DESC")
    List<Notification> findReadByRecipient(@Param("user") User user);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.recipient = :user AND n.isRead = false AND n.deletedAt IS NULL")
    long countUnreadByRecipient(@Param("user") User user);

    @Query("SELECT n FROM Notification n WHERE n.recipient = :user AND n.type = :type AND n.deletedAt IS NULL ORDER BY n.createdAt DESC")
    List<Notification> findByRecipientAndType(@Param("user") User user, @Param("type") Notification.NotificationType type);

    @Query("SELECT n FROM Notification n WHERE n.sender = :user AND n.deletedAt IS NULL ORDER BY n.createdAt DESC")
    List<Notification> findBySender(@Param("user") User user);
}