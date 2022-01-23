package net.therap.leavemanagement.dao;

import net.therap.leavemanagement.domain.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author rumi.dipto
 * @since 1/23/22
 */
@Repository
public interface NotificationRepo extends CrudRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId")
    List<Notification> findAllByUserId(long userId);

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.seen = FALSE")
    List<Notification> findAllUnseenNotifications(long userId);
}
