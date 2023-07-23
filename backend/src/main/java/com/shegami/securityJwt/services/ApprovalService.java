package com.shegami.securityJwt.services;

import com.shegami.securityJwt.entities.Absence;
import com.shegami.securityJwt.entities.AppUser;
import com.shegami.securityJwt.entities.Approval;

import java.util.List;

public interface ApprovalService {

    List<Approval> listAllApprovals();

    Approval findByUserUsername(String username);

    Approval getApprovalById(String id);

    Approval approveRequest(Approval approval);

    void deleteApproval(String id);

    Approval updateApproval(Approval approval);
}
