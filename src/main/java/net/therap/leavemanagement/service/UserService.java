package net.therap.leavemanagement.service;

import net.therap.leavemanagement.dao.UserRepo;
import net.therap.leavemanagement.domain.Designation;
import net.therap.leavemanagement.domain.User;
import net.therap.leavemanagement.util.HashGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author rumi.dipto
 * @since 11/24/21
 */
@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private LeaveStatService leaveStatService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private NotificationService notificationService;

    public User findById(long id) {
        return userRepo.findById(id);
    }

    public User findHrExecutive() {
        return userRepo.findHrExecutive();
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public List<User> findAllTeamLead(Designation designation) {
        return userRepo.findAllByDesignation(designation);
    }

    public List<User> findAllTeamLead(Designation designation, Pageable pageable) {
        return userRepo.findAllByDesignation(designation, pageable);
    }

    public List<User> findAllUserByDesignation(User sessionUser, Designation designation, Pageable pageable) {
        switch (sessionUser.getDesignation()) {
            case HR_EXECUTIVE:
                return userRepo.findAllByDesignation(designation, pageable);
            case TEAM_LEAD:
                return userManagementService.findAllUserByDesignationUnderTeamLead(sessionUser.getId(), designation, pageable);
            default:
                return null;
        }
    }

    public long countTeamLead(Designation designation) {
        return userRepo.countByDesignation(designation);
    }

    public long countUserByDesignation(User sessionUser, Designation designation) {
        switch (sessionUser.getDesignation()) {
            case HR_EXECUTIVE:
                return userRepo.countByDesignation(designation);
            case TEAM_LEAD:
                return userManagementService.countUserByDesignationUnderTeamLead(sessionUser.getId(), designation);
            default:
                return 0;
        }
    }

    @Transactional
    public void saveOrUpdate(User user, User teamLead) {
        if (user.isNew()) {
            user.setPassword(HashGenerator.getMd5(user.getPassword()));
            user.setActivated(false);

            userRepo.save(user);
            userManagementService.createAndSaveWithNewUser(user, teamLead);
            leaveStatService.createAndSaveWithNewUser(user);
        } else {

            if (isDesignationChanged(user)) {
                userManagementService.deleteWithUserDesignationUpdate(user);
                leaveService.updateLeaveStatusWithUserDesignationUpdate(user);
            } else if (isTeamLeadChanged(user, teamLead)) {
                userManagementService.updateTeamLeadWithUserUpdate(user, teamLead);
            }

            userRepo.save(user);
        }
    }

    @Transactional
    public void updatePassword(User user) {
        if (!user.isActivated()) {
            user.setActivated(true);
        }
        userRepo.save(user);
    }

    @Transactional
    public void delete(User user) {
        leaveStatService.deleteByUser(user);

        notificationService.deleteByUser(user);

        leaveService.deleteByUser(user);

        userManagementService.deleteByUser(user);

        userRepo.delete(user);
    }

    public boolean isDesignationChanged(User commandUser) {
        long id = commandUser.getId();

        if (id != 0) {
            User dbUser = userRepo.findById(id);
            return (commandUser.isTeamLead() && ((dbUser.isDeveloper()) || (dbUser.isTester())));
        }

        return false;
    }

    public boolean isTeamLeadChanged(User commandUser, User commandTeamLead) {
        if (Objects.nonNull(commandTeamLead) && (commandUser.isDeveloper() || commandUser.isTester())) {
            User dbTeamLead = userManagementService.findTeamLeadByUserId(commandUser.getId());
            return !(commandTeamLead.equals(dbTeamLead));
        }

        return false;
    }
}
