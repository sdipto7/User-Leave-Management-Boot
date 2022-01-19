package net.therap.leavemanagement.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

import static net.therap.leavemanagement.domain.LeaveStatus.*;

/**
 * @author rumi.dipto
 * @since 11/22/21
 */
@Getter
@Setter
@Entity
@Table(name = "lm_leave_request")
@NamedQueries({
        @NamedQuery(name = "Leave.findAllLeavesOfUser",
                query = "SELECT l FROM Leave l WHERE l.user.id = :userId ORDER BY l.id"),

        @NamedQuery(name = "Leave.findProceededLeavesOfUser",
                query = "SELECT l FROM Leave l WHERE l.user.id = :userId AND " +
                        "(l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
                        "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD') ORDER BY l.id"),

        @NamedQuery(name = "Leave.findPendingLeavesOfUser",
                query = "SELECT l FROM Leave l WHERE l.user.id = :userId AND " +
                        "(l.leaveStatus = 'PENDING_BY_TEAM_LEAD' OR l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE') ORDER BY l.id"),

        @NamedQuery(name = "Leave.findAllProceededLeavesUnderTeamLead",
                query = "SELECT l FROM Leave l " +
                        "WHERE (l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
                        "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD') AND EXISTS (SELECT 1 FROM UserManagement um WHERE " +
                        "l.user.id = um.user.id AND um.teamLead.id = :teamLeadId) ORDER BY l.id"),

        @NamedQuery(name = "Leave.findAllPendingLeavesUnderTeamLead",
                query = "SELECT l FROM Leave l " +
                        "WHERE (l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE' OR l.leaveStatus = 'PENDING_BY_TEAM_LEAD') AND " +
                        "EXISTS (SELECT 1 FROM UserManagement um WHERE " +
                        "l.user.id = um.user.id AND um.teamLead.id = :teamLeadId) ORDER BY l.id"),

        @NamedQuery(name = "Leave.findAllProceededLeaves",
                query = "SELECT l FROM Leave l WHERE " +
                        "l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
                        "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD' ORDER BY l.id"),

        @NamedQuery(name = "Leave.findAllPendingLeaves",
                query = "SELECT l FROM Leave l WHERE " +
                        "l.leaveStatus = 'PENDING_BY_TEAM_LEAD' OR l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE' ORDER BY l.id"),

        @NamedQuery(name = "Leave.countProceededLeavesOfUser",
                query = "SELECT COUNT(l) FROM Leave l WHERE l.user.id = :userId AND " +
                        "(l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
                        "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD')"),

        @NamedQuery(name = "Leave.countPendingLeavesOfUser",
                query = "SELECT COUNT(l) FROM Leave l WHERE l.user.id = :userId AND " +
                        "(l.leaveStatus = 'PENDING_BY_TEAM_LEAD' OR l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE')"),

        @NamedQuery(name = "Leave.countAllProceededLeavesUnderTeamLead",
                query = "SELECT COUNT(l) FROM Leave l WHERE " +
                        "(l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
                        "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD') AND EXISTS (SELECT 1 FROM UserManagement um WHERE " +
                        "l.user.id = um.user.id AND um.teamLead.id = :teamLeadId)"),

        @NamedQuery(name = "Leave.countAllPendingLeavesUnderTeamLead",
                query = "SELECT COUNT(l) FROM Leave l WHERE " +
                        "(l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE' OR l.leaveStatus = 'PENDING_BY_TEAM_LEAD') AND " +
                        "EXISTS (SELECT 1 FROM UserManagement um WHERE " +
                        "l.user.id = um.user.id AND um.teamLead.id = :teamLeadId)"),

        @NamedQuery(name = "Leave.countAllProceededLeaves",
                query = "SELECT COUNT(l) FROM Leave l WHERE " +
                        "l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
                        "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD'"),

        @NamedQuery(name = "Leave.countAllPendingLeaves",
                query = "SELECT COUNT(l) FROM Leave l WHERE " +
                        "l.leaveStatus = 'PENDING_BY_TEAM_LEAD' OR l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE'")
})
public class Leave extends Persistent {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type")
    @NotNull
    private LeaveType leaveType;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_status")
    @NotNull
    private LeaveStatus leaveStatus;

    @Size(min = 2, max = 100)
    @NotNull
    private String note;

    @Column(name = "start_date")
    @NotNull
    private Date startDate;

    @Column(name = "end_date")
    @NotNull
    private Date endDate;

    public boolean isPendingByHrExecutive() {
        return this.leaveStatus.equals(PENDING_BY_HR_EXECUTIVE);
    }

    public boolean isApprovedByHrExecutive() {
        return this.leaveStatus.equals(APPROVED_BY_HR_EXECUTIVE);
    }

    public boolean isDeniedByHrExecutive() {
        return this.leaveStatus.equals(DENIED_BY_HR_EXECUTIVE);
    }

    public boolean isPendingByTeamLead() {
        return this.leaveStatus.equals(PENDING_BY_TEAM_LEAD);
    }

    public boolean isDeniedByTeamLead() {
        return this.leaveStatus.equals(DENIED_BY_TEAM_LEAD);
    }
}
