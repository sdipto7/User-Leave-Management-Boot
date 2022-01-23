package net.therap.leavemanagement.dao;

import net.therap.leavemanagement.domain.User;
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
public interface UserRepo extends CrudRepository<User, Long> {

    User findById(long id);

    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.designation = 'HR_EXECUTIVE'")
    User findHrExecutive();

    @Query("SELECT u FROM User u WHERE u.designation = 'TEAM_LEAD' ORDER BY u.id ASC")
    List<User> findAllTeamLead();

    @Query("SELECT u FROM User u WHERE u.designation = 'TEAM_LEAD' ORDER BY u.id ASC")
    List<User> findAllTeamLead(Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.designation = 'TEAM_LEAD'")
    long countTeamLead();

    @Query("SELECT u FROM User u WHERE u.designation = 'DEVELOPER' ORDER BY u.id ASC")
    List<User> findAllDeveloper(Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.designation = 'DEVELOPER'")
    long countDeveloper();

    @Query("SELECT u FROM User u WHERE u.designation = 'TESTER' ORDER BY u.id ASC")
    List<User> findAllTester(Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.designation = 'TESTER'")
    long countTester();
}
