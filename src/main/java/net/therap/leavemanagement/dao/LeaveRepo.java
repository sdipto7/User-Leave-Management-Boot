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

    String findAllLeavesOfUserQuery = "SELECT l FROM Leave l WHERE l.user.id = :userId ORDER BY l.id";

    String findProceededLeavesOfUserQuery = "SELECT l FROM Leave l WHERE l.user.id = :userId AND " +
            "(l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
            "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD') ORDER BY l.id";

    String findPendingLeavesOfUserQuery = "SELECT l FROM Leave l WHERE l.user.id = :userId AND " +
            "(l.leaveStatus = 'PENDING_BY_TEAM_LEAD' OR l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE') ORDER BY l.id";

    String findAllProceededLeavesUnderTeamLeadQuery = "SELECT l FROM Leave l " +
            "WHERE (l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
            "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD') AND EXISTS (SELECT 1 FROM UserManagement um WHERE " +
            "l.user.id = um.user.id AND um.teamLead.id = :teamLeadId) ORDER BY l.id";

    String findAllPendingLeavesUnderTeamLeadQuery = "SELECT l FROM Leave l " +
            "WHERE (l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE' OR l.leaveStatus = 'PENDING_BY_TEAM_LEAD') AND " +
            "EXISTS (SELECT 1 FROM UserManagement um WHERE " +
            "l.user.id = um.user.id AND um.teamLead.id = :teamLeadId) ORDER BY l.id";

    String findAllProceededLeavesQuery = "SELECT l FROM Leave l WHERE " +
            "l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
            "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD' ORDER BY l.id";

    String findAllPendingLeavesQuery = "SELECT l FROM Leave l WHERE " +
            "l.leaveStatus = 'PENDING_BY_TEAM_LEAD' OR l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE' ORDER BY l.id";

    String countProceededLeavesOfUserQuery = "SELECT COUNT(l) FROM Leave l WHERE l.user.id = :userId AND " +
            "(l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
            "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD')";

    String countPendingLeavesOfUserQuery = "SELECT COUNT(l) FROM Leave l WHERE l.user.id = :userId AND " +
            "(l.leaveStatus = 'PENDING_BY_TEAM_LEAD' OR l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE')";

    String countAllProceededLeavesUnderTeamLeadQuery = "SELECT COUNT(l) FROM Leave l WHERE " +
            "(l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
            "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD') AND EXISTS (SELECT 1 FROM UserManagement um WHERE " +
            "l.user.id = um.user.id AND um.teamLead.id = :teamLeadId)";

    String countAllPendingLeavesUnderTeamLeadQuery = "SELECT COUNT(l) FROM Leave l WHERE " +
            "(l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE' OR l.leaveStatus = 'PENDING_BY_TEAM_LEAD') AND " +
            "EXISTS (SELECT 1 FROM UserManagement um WHERE " +
            "l.user.id = um.user.id AND um.teamLead.id = :teamLeadId)";

    String countAllProceededLeavesQuery = "SELECT COUNT(l) FROM Leave l WHERE " +
            "l.leaveStatus = 'APPROVED_BY_HR_EXECUTIVE' OR l.leaveStatus = 'DENIED_BY_HR_EXECUTIVE' " +
            "OR l.leaveStatus = 'DENIED_BY_TEAM_LEAD'";

    String countAllPendingLeavesQuery = "SELECT COUNT(l) FROM Leave l WHERE " +
            "l.leaveStatus = 'PENDING_BY_TEAM_LEAD' OR l.leaveStatus = 'PENDING_BY_HR_EXECUTIVE'";

    Leave findById(long id);

    @Query(findAllLeavesOfUserQuery)
    List<Leave> findAllLeavesOfUser(long userId);

    @Query(findProceededLeavesOfUserQuery)
    List<Leave> findProceededLeavesOfUser(long userId, Pageable pageable);

    @Query(findPendingLeavesOfUserQuery)
    List<Leave> findPendingLeavesOfUser(long userId);

    @Query(findPendingLeavesOfUserQuery)
    List<Leave> findPendingLeavesOfUser(long userId, Pageable pageable);

    @Query(findAllProceededLeavesUnderTeamLeadQuery)
    List<Leave> findAllProceededLeavesUnderTeamLead(long teamLeadId, Pageable pageable);

    @Query(findAllPendingLeavesUnderTeamLeadQuery)
    List<Leave> findAllPendingLeavesUnderTeamLead(long teamLeadId, Pageable pageable);

    @Query(findAllProceededLeavesQuery)
    List<Leave> findAllProceededLeaves(Pageable pageable);

    @Query(findAllPendingLeavesQuery)
    List<Leave> findAllPendingLeaves(Pageable pageable);

    @Query(countProceededLeavesOfUserQuery)
    long countProceededLeavesOfUser(long userId);

    @Query(countPendingLeavesOfUserQuery)
    long countPendingLeavesOfUser(long userId);

    @Query(countAllProceededLeavesUnderTeamLeadQuery)
    long countAllProceededLeavesUnderTeamLead(long teamLeadId);

    @Query(countAllPendingLeavesUnderTeamLeadQuery)
    long countAllPendingLeavesUnderTeamLead(long teamLeadId);

    @Query(countAllProceededLeavesQuery)
    long countAllProceededLeaves();

    @Query(countAllPendingLeavesQuery)
    long countAllPendingLeaves();
}
