package net.therap.leavemanagement.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author rumi.dipto
 * @since 12/12/21
 */
@Getter
@Setter
@Entity
@Table(name = "lm_notification")
@NamedQueries({
        @NamedQuery(name = "Notification.findAllNotifications",
                query = "SELECT n FROM Notification n WHERE n.user.id = :userId"),

        @NamedQuery(name = "Notification.findAllUnseenNotifications",
                query = "SELECT n FROM Notification n WHERE n.user.id = :userId AND n.seen = FALSE")
})
public class Notification extends Persistent {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Size(min = 2, max = 100)
    @NotNull
    private String message;

    @NotNull
    private boolean seen;
}
