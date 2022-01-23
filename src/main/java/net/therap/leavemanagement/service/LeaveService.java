package net.therap.leavemanagement.service;

import net.therap.leavemanagement.dao.LeaveRepo;
import net.therap.leavemanagement.domain.Leave;
import net.therap.leavemanagement.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static net.therap.leavemanagement.domain.LeaveStatus.PENDING_BY_HR_EXECUTIVE;

/**
 * @author rumi.dipto
 * @since 12/1/21
 */
@Service
public class LeaveService {

    @Autowired
    private LeaveRepo leaveRepo;

    @Autowired
    private LeaveStatService leaveStatService;

    public Leave find(long id) {
        return leaveRepo.findById(id);
    }

    public List<Leave> findProceededLeavesOfUser(long userId, Pageable pageable) {
        return leaveRepo.findProceededLeavesOfUser(userId, pageable);
    }

    public List<Leave> findPendingLeavesOfUser(long userId) {
        return leaveRepo.findPendingLeavesOfUser(userId);
    }

    public List<Leave> findPendingLeavesOfUser(long userId, Pageable pageable) {
        return leaveRepo.findPendingLeavesOfUser(userId, pageable);
    }

    public List<Leave> findAllProceededLeaves(User sessionUser, Pageable pageable) {
        switch (sessionUser.getDesignation()) {
            case HR_EXECUTIVE:
                return leaveRepo.findAllProceededLeaves(pageable);
            case TEAM_LEAD:
                return leaveRepo.findAllProceededLeavesUnderTeamLead(sessionUser.getId(), pageable);
            default:
                return null;
        }
    }

    public List<Leave> findAllPendingLeaves(User sessionUser, Pageable pageable) {
        switch (sessionUser.getDesignation()) {
            case HR_EXECUTIVE:
                return leaveRepo.findAllPendingLeaves(pageable);
            case TEAM_LEAD:
                return leaveRepo.findAllPendingLeavesUnderTeamLead(sessionUser.getId(), pageable);
            default:
                return null;
        }
    }

    public long countProceededLeavesOfUser(long userId) {
        return leaveRepo.countProceededLeavesOfUser(userId);
    }

    public long countPendingLeavesOfUser(long userId) {
        return leaveRepo.countPendingLeavesOfUser(userId);
    }

    public long countAllProceededLeaves(User sessionUser) {
        switch (sessionUser.getDesignation()) {
            case HR_EXECUTIVE:
                return leaveRepo.countAllProceededLeaves();
            case TEAM_LEAD:
                return leaveRepo.countAllProceededLeavesUnderTeamLead(sessionUser.getId());
            default:
                return 0;
        }
    }

    public long countAllPendingLeaves(User sessionUser) {
        switch (sessionUser.getDesignation()) {
            case HR_EXECUTIVE:
                return leaveRepo.countAllPendingLeaves();
            case TEAM_LEAD:
                return leaveRepo.countAllPendingLeavesUnderTeamLead(sessionUser.getId());
            default:
                return 0;
        }
    }

    @Transactional
    public void saveOrUpdate(Leave leave) {
        if (leave.isApprovedByHrExecutive()) {
            leaveStatService.updateByLeave(leave);
        }

        leaveRepo.save(leave);
    }

    @Transactional
    public void updateLeaveStatusWithUserDesignationUpdate(User user) {
        List<Leave> pendingLeaveList = leaveRepo.findPendingLeavesOfUser(user.getId());
        for (Leave pendingLeave : pendingLeaveList) {
            if (pendingLeave.isPendingByTeamLead()) {
                pendingLeave.setLeaveStatus(PENDING_BY_HR_EXECUTIVE);
                leaveRepo.save(pendingLeave);
            }
        }
    }

    @Transactional
    public void delete(Leave leave) {
        leaveRepo.delete(leave);
    }

    @Transactional
    public void deleteByUser(User user) {
        List<Leave> leaveList = leaveRepo.findAllLeavesOfUser(user.getId());
        if (leaveList.size() > 0) {
            leaveList.forEach(leave -> leaveRepo.delete(leave));
        }
    }
}
