package net.therap.leavemanagement.dao;

import net.therap.leavemanagement.domain.Leave;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author rumi.dipto
 * @since 1/24/22
 */
@Repository
public interface LeaveRepo extends CrudRepository<Leave, Long> {

    Leave findById(long id);

    @Query("SELECT l FROM Leave l WHERE l.user.id = :userId ORDER BY l.id")
    List<Leave> findAllLeavesOfUser(long userId);

    @Query("SELECT l FROM Leave l WHERE l.user.id = :userId AND " +
            "(l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
            "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD') ORDER BY l.id")
    List<Leave> findProceededLeavesOfUser(long userId, Pageable pageable);

    @Query("SELECT l FROM Leave l WHERE l.user.id = :userId AND " +
            "(l.leaveStatus = 'PENDING_BY_TEAM_LEAD' OR l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE') ORDER BY l.id")
    List<Leave> findPendingLeavesOfUser(long userId);

    @Query("SELECT l FROM Leave l WHERE l.user.id = :userId AND " +
            "(l.leaveStatus = 'PENDING_BY_TEAM_LEAD' OR l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE') ORDER BY l.id")
    List<Leave> findPendingLeavesOfUser(long userId, Pageable pageable);

    @Query("SELECT l FROM Leave l " +
            "WHERE (l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
            "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD') AND EXISTS (SELECT 1 FROM UserManagement um WHERE " +
            "l.user.id = um.user.id AND um.teamLead.id = :teamLeadId) ORDER BY l.id")
    List<Leave> findAllProceededLeavesUnderTeamLead(long teamLeadId, Pageable pageable);

    @Query("SELECT l FROM Leave l " +
            "WHERE (l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE' OR l.leaveStatus = 'PENDING_BY_TEAM_LEAD') AND " +
            "EXISTS (SELECT 1 FROM UserManagement um WHERE " +
            "l.user.id = um.user.id AND um.teamLead.id = :teamLeadId) ORDER BY l.id")
    List<Leave> findAllPendingLeavesUnderTeamLead(long teamLeadId, Pageable pageable);

    @Query("SELECT l FROM Leave l WHERE " +
            "l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
            "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD' ORDER BY l.id")
    List<Leave> findAllProceededLeaves(Pageable pageable);

    @Query("SELECT l FROM Leave l WHERE " +
            "l.leaveStatus = 'PENDING_BY_TEAM_LEAD' OR l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE' ORDER BY l.id")
    List<Leave> findAllPendingLeaves(Pageable pageable);

    @Query("SELECT COUNT(l) FROM Leave l WHERE l.user.id = :userId AND " +
            "(l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
            "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD')")
    long countProceededLeavesOfUser(long userId);

    @Query("SELECT COUNT(l) FROM Leave l WHERE l.user.id = :userId AND " +
            "(l.leaveStatus = 'PENDING_BY_TEAM_LEAD' OR l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE')")
    long countPendingLeavesOfUser(long userId);

    @Query("SELECT COUNT(l) FROM Leave l WHERE " +
            "(l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
            "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD') AND EXISTS (SELECT 1 FROM UserManagement um WHERE " +
            "l.user.id = um.user.id AND um.teamLead.id = :teamLeadId)")
    long countAllProceededLeavesUnderTeamLead(long teamLeadId);

    @Query("SELECT COUNT(l) FROM Leave l WHERE " +
            "(l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE' OR l.leaveStatus = 'PENDING_BY_TEAM_LEAD') AND " +
            "EXISTS (SELECT 1 FROM UserManagement um WHERE " +
            "l.user.id = um.user.id AND um.teamLead.id = :teamLeadId)")
    long countAllPendingLeavesUnderTeamLead(long teamLeadId);

    @Query("SELECT COUNT(l) FROM Leave l WHERE " +
            "l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
            "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD'")
    long countAllProceededLeaves();

    @Query("SELECT COUNT(l) FROM Leave l WHERE " +
            "l.leaveStatus = 'PENDING_BY_TEAM_LEAD' OR l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE'")
    long countAllPendingLeaves();
}
