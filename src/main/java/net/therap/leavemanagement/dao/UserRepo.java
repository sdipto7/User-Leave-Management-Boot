package net.therap.leavemanagement.dao;

import net.therap.leavemanagement.domain.Designation;
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

    String findHrExecutiveQuery = "SELECT u FROM User u WHERE u.designation = 'HR_EXECUTIVE'";

    String findAllByDesignationQuery = "SELECT u FROM User u WHERE u.designation = :designation ORDER BY u.id ASC";

    User findById(long id);

    User findByUsername(String username);

    @Query(findHrExecutiveQuery)
    User findHrExecutive();

    @Query(findAllByDesignationQuery)
    List<User> findAllByDesignation(Designation designation);

    @Query(findAllByDesignationQuery)
    List<User> findAllByDesignation(Designation designation, Pageable pageable);

    long countByDesignation(Designation designation);
}
