package net.therap.leavemanagement.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author rumi.dipto
 * @since 11/22/21
 */
@Entity
@Table(name = "lm_leave_stat")
@NamedQueries({
        @NamedQuery(name = "LeaveStat.findLeaveStatByUserId",
                query = "SELECT ls FROM LeaveStat ls WHERE ls.user.id = :id")
})
public class LeaveStat extends Persistent {

    private static final long serialVersionUID = 1L;

    @OneToOne(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Column(name = "sick_leave_count")
    private int sickLeaveCount;

    @Column(name = "casual_leave_count")
    private int casualLeaveCount;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getSickLeaveCount() {
        return sickLeaveCount;
    }

    public void setSickLeaveCount(int sickLeaveCount) {
        this.sickLeaveCount = sickLeaveCount;
    }

    public int getCasualLeaveCount() {
        return casualLeaveCount;
    }

    public void setCasualLeaveCount(int casualLeaveCount) {
        this.casualLeaveCount = casualLeaveCount;
    }
}
