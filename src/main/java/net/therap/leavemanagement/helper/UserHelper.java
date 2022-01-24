package net.therap.leavemanagement.helper;

import net.therap.leavemanagement.domain.Designation;
import net.therap.leavemanagement.domain.User;
import net.therap.leavemanagement.service.LeaveStatService;
import net.therap.leavemanagement.service.UserManagementService;
import net.therap.leavemanagement.service.UserService;
import net.therap.leavemanagement.util.HashGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

import static net.therap.leavemanagement.domain.Designation.*;
import static net.therap.leavemanagement.util.Constant.ITEMS_PER_PAGE;

/**
 * @author rumi.dipto
 * @since 11/28/21
 */
@Component
public class UserHelper {

    @Autowired
    private UserService userService;

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private LeaveStatService leaveStatService;

    @Autowired
    private AuthorizationHelper authorizationHelper;

    public void setupDataIfTeamLead(User user, ModelMap modelMap) {
        if (user.isTeamLead()) {
            modelMap.addAttribute("developerList",
                    userManagementService.findAllUserByDesignationUnderTeamLead(user.getId(), DEVELOPER));
            modelMap.addAttribute("testerList",
                    userManagementService.findAllUserByDesignationUnderTeamLead(user.getId(), TESTER));
        }
    }

    public int getTotalPageNumber(int listSize) {
        return ((listSize % ITEMS_PER_PAGE == 0) ? (listSize / ITEMS_PER_PAGE) : (listSize / ITEMS_PER_PAGE) + 1);
    }

    public User getOrCreateUser(long id) {
        return id == 0 ? new User() : userService.findById(id);
    }

    public void checkAndAddAuthorizedTeamLeadIfExist(User user, HttpSession session, ModelMap modelMap) {
        User sessionUser = (User) session.getAttribute("SESSION_USER");
        User teamLead = userManagementService.findTeamLeadByUserId(user.getId());

        if ((user.isDeveloper() || user.isTester()) && (sessionUser.isTeamLead())) {
            authorizationHelper.checkAccess(teamLead);
        }
        modelMap.addAttribute("teamLead", teamLead);
    }

    public void setupNewPasswordForUser(User user, String newPassword) {
        user.setPassword(HashGenerator.getMd5(newPassword));
    }

    public void setDataForUserSaveForm(ModelMap modelMap) {
        modelMap.addAttribute("teamLeadList", userService.findAllUserByDesignation(TEAM_LEAD));
        modelMap.addAttribute("designationList", Arrays.asList(Designation.values()));
    }

    public void setDataForUpdatePasswordForm(User user, ModelMap modelMap) {
        modelMap.addAttribute("teamLead", userManagementService.findTeamLeadByUserId(user.getId()));
        modelMap.addAttribute("leaveStat", leaveStatService.findLeaveStatByUserId(user.getId()));
    }

    public void setConditionalDataForUserSaveView(User user, ModelMap modelMap) {
        if (user.isNew() || (!user.isNew() && (user.isDeveloper() || user.isTester()))) {
            modelMap.addAttribute("canSelectDesignation", true);
        }

        if (user.isNew()) {
            modelMap.addAttribute("canInputPassword", true);
        }
    }
}
