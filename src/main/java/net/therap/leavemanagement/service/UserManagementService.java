package net.therap.leavemanagement.service;

import net.therap.leavemanagement.dao.UserManagementRepo;
import net.therap.leavemanagement.domain.User;
import net.therap.leavemanagement.domain.UserManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author rumi.dipto
 * @since 11/27/21
 */
@Service
public class UserManagementService {

    @Autowired
    private UserManagementRepo userManagementRepo;

    public User findTeamLeadByUserId(long userId) {
        return userManagementRepo.findTeamLeadByUserId(userId);
    }

    public List<User> findAllDeveloperUnderTeamLead(long teamLeadId) {
        return userManagementRepo.findAllDeveloperUnderTeamLead(teamLeadId);
    }

    public List<User> findAllDeveloperUnderTeamLead(long teamLeadId, Pageable pageable) {
        return userManagementRepo.findAllDeveloperUnderTeamLead(teamLeadId, pageable);
    }

    public long countDeveloperUnderTeamLead(long teamLeadId) {
        return userManagementRepo.countDeveloperUnderTeamLead(teamLeadId);
    }

    public List<User> findAllTesterUnderTeamLead(long teamLeadId) {
        return userManagementRepo.findAllTesterUnderTeamLead(teamLeadId);
    }

    public List<User> findAllTesterUnderTeamLead(long teamLeadId, Pageable pageable) {
        return userManagementRepo.findAllTesterUnderTeamLead(teamLeadId, pageable);
    }

    public long countTesterUnderTeamLead(long teamLeadId) {
        return userManagementRepo.countTesterUnderTeamLead(teamLeadId);
    }

    @Transactional
    public void saveOrUpdate(UserManagement userManagement) {
        userManagementRepo.save(userManagement);
    }

    @Transactional
    public void createAndSaveWithNewUser(User user, User teamLead) {
        if (canHaveTeamLead(user, teamLead)) {
            UserManagement userManagement = new UserManagement();
            userManagement.setUser(user);
            userManagement.setTeamLead(teamLead);

            userManagementRepo.save(userManagement);
        }
    }

    @Transactional
    public void updateTeamLeadWithUserUpdate(User user, User teamLead) {
        UserManagement userManagement = userManagementRepo.findByUserId(user.getId());

        if (Objects.isNull(userManagement)) {
            userManagement = new UserManagement();
            userManagement.setUser(user);
        }
        userManagement.setTeamLead(teamLead);

        userManagementRepo.save(userManagement);
    }

    @Transactional
    public void delete(UserManagement userManagement) {
        userManagementRepo.delete(userManagement);
    }

    @Transactional
    public void deleteByUser(User user) {
        long userId = user.getId();

        if (user.isTeamLead()) {
            List<UserManagement> userManagementList = userManagementRepo.findAllByTeamLeadId(userId);
            if (userManagementList.size() > 0) {
                userManagementList.forEach(userManagement -> userManagementRepo.delete(userManagement));
            }
        } else if (user.isDeveloper() || user.isTester()) {
            UserManagement userManagement = userManagementRepo.findByUserId(userId);
            if (Objects.nonNull(userManagement)) {
                userManagementRepo.delete(userManagement);
            }
        }
    }

    @Transactional
    public void deleteWithUserDesignationUpdate(User user) {
        UserManagement userManagement = userManagementRepo.findByUserId(user.getId());
        userManagementRepo.delete(userManagement);
    }

    public boolean canHaveTeamLead(User user, User teamLead) {
        return (Objects.nonNull(teamLead) && (user.isDeveloper() || user.isTester()));
    }
}
