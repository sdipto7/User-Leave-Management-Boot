package net.therap.leavemanagement.dao;

import net.therap.leavemanagement.domain.User;
import net.therap.leavemanagement.domain.UserManagement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author rumi.dipto
 * @since 1/23/22
 */
@Repository
public interface UserManagementRepo extends CrudRepository<UserManagement, Long> {

    @Query("SELECT um FROM UserManagement um WHERE um.user.id = :userId")
    UserManagement findByUserId(long userId);

    @Query("SELECT um FROM UserManagement um WHERE um.teamLead.id = :teamLeadId")
    List<UserManagement> findAllByTeamLeadId(long teamLeadId);

    @Query("SELECT um.teamLead FROM UserManagement um WHERE um.user.id = :userId")
    User findTeamLeadByUserId(long userId);

    @Query("SELECT um.user FROM UserManagement um " +
            "WHERE um.teamLead.id = :teamLeadId AND um.user.designation = 'DEVELOPER' ORDER BY um.id")
    List<User> findAllDeveloperUnderTeamLead(long teamLeadId);

    @Query("SELECT um.user FROM UserManagement um " +
            "WHERE um.teamLead.id = :teamLeadId AND um.user.designation = 'DEVELOPER' ORDER BY um.id")
    List<User> findAllDeveloperUnderTeamLead(long teamLeadId, Pageable pageable);

    @Query("SELECT COUNT(um) FROM UserManagement um WHERE um.teamLead.id = :teamLeadId AND um.user.designation = 'DEVELOPER'")
    long countDeveloperUnderTeamLead(long teamLeadId);

    @Query("SELECT um.user FROM UserManagement um " +
            "WHERE um.teamLead.id = :teamLeadId AND um.user.designation = 'TESTER' ORDER BY um.id")
    List<User> findAllTesterUnderTeamLead(long teamLeadId);

    @Query("SELECT um.user FROM UserManagement um " +
            "WHERE um.teamLead.id = :teamLeadId AND um.user.designation = 'TESTER' ORDER BY um.id")
    List<User> findAllTesterUnderTeamLead(long teamLeadId, Pageable pageable);

    @Query("SELECT COUNT(um) FROM UserManagement um WHERE um.teamLead.id = :teamLeadId AND um.user.designation = 'TESTER'")
    long countTesterUnderTeamLead(long teamLeadId);
}
