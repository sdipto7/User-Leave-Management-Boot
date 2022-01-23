package net.therap.leavemanagement.service;

import net.therap.leavemanagement.dao.NotificationRepo;
import net.therap.leavemanagement.domain.Notification;
import net.therap.leavemanagement.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author rumi.dipto
 * @since 12/12/21
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    public List<Notification> findAllUnseenNotifications(long userId) {
        return notificationRepo.findAllUnseenNotifications(userId);
    }

    @Transactional
    public void saveOrUpdate(Notification notification) {
        notificationRepo.save(notification);
    }

    @Transactional
    public void deleteByUser(User user) {
        List<Notification> notificationList = notificationRepo.findAllByUserId(user.getId());
        if (notificationList.size() > 0) {
            notificationList.forEach(notification -> notificationRepo.delete(notification));
        }
    }
}
