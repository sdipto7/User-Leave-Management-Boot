package net.therap.leavemanagement.service;

import net.therap.leavemanagement.dao.UserRepo;
import net.therap.leavemanagement.domain.Designation;
import net.therap.leavemanagement.domain.User;
import net.therap.leavemanagement.util.HashGenerator;
import net.therap.leavemanagement.util.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import java.util.List;
import java.util.Objects;

import static net.therap.leavemanagement.domain.Designation.DEVELOPER;
import static net.therap.leavemanagement.domain.Designation.TESTER;

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

    public List<User> findAllUserByDesignation(Designation designation) {
        return userRepo.findAllByDesignation(designation);
    }

    public List<User> findAllUserByDesignation(Designation designation, Pageable pageable) {
        User sessionUser = (User) WebUtils.getSessionAttribute(ServletUtil.getHttpServletRequest(), "SESSION_USER");

        if (sessionUser.isHrExecutive()) {
            return userRepo.findAllByDesignation(designation, pageable);
        } else if (sessionUser.isTeamLead() && (designation.equals(DEVELOPER) || designation.equals(TESTER))) {
            return userManagementService.findAllUserByDesignationUnderTeamLead(sessionUser.getId(), designation, pageable);
        } else {
            return null;
        }
    }

    public long countUserByDesignation(Designation designation) {
        User sessionUser = (User) WebUtils.getSessionAttribute(ServletUtil.getHttpServletRequest(), "SESSION_USER");

        if (sessionUser.isHrExecutive()) {
            return userRepo.countByDesignation(designation);
        } else if (sessionUser.isTeamLead() && (designation.equals(DEVELOPER) || designation.equals(TESTER))) {
            return userManagementService.countUserByDesignationUnderTeamLead(sessionUser.getId(), designation);
        } else {
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
