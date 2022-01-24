package net.therap.leavemanagement.dao;

import net.therap.leavemanagement.domain.Designation;
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

    String findByUserIdQuery = "SELECT um FROM UserManagement um WHERE um.user.id = :userId";

    String findAllByTeamLeadIdQuery = "SELECT um FROM UserManagement um WHERE um.teamLead.id = :teamLeadId";

    String findTeamLeadByUserIdQuery = "SELECT um.teamLead FROM UserManagement um WHERE um.user.id = :userId";

    String findAllUserByDesignationUnderTeamLeadQuery = "SELECT um.user FROM UserManagement um " +
            "WHERE um.teamLead.id = :teamLeadId AND um.user.designation = :designation ORDER BY um.id";

    String countUserByDesignationUnderTeamLeadQuery = "SELECT COUNT(um) FROM UserManagement um WHERE um.teamLead.id = :teamLeadId AND um.user.designation = :designation";

    @Query(findByUserIdQuery)
    UserManagement findByUserId(long userId);

    @Query(findAllByTeamLeadIdQuery)
    List<UserManagement> findAllByTeamLeadId(long teamLeadId);

    @Query(findTeamLeadByUserIdQuery)
    User findTeamLeadByUserId(long userId);

    @Query(findAllUserByDesignationUnderTeamLeadQuery)
    List<User> findAllUserByDesignationUnderTeamLead(long teamLeadId, Designation designation);

    @Query(findAllUserByDesignationUnderTeamLeadQuery)
    List<User> findAllUserByDesignationUnderTeamLead(long teamLeadId, Designation designation, Pageable pageable);

    @Query(countUserByDesignationUnderTeamLeadQuery)
    long countUserByDesignationUnderTeamLead(long teamLeadId, Designation designation);
}
