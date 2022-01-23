package net.therap.leavemanagement.dao;

import net.therap.leavemanagement.domain.LeaveStat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rumi.dipto
 * @since 1/23/22
 */
@Repository
public interface LeaveStatRepo extends CrudRepository<LeaveStat, Long> {

    @Query("SELECT ls FROM LeaveStat ls WHERE ls.user.id = :userId")
    LeaveStat findByUserId(long userId);
}
