package com.shegami.securityJwt.services;

import com.shegami.securityJwt.entities.Absence;
import com.shegami.securityJwt.entities.AppUser;
import com.shegami.securityJwt.entities.Approval;
import com.shegami.securityJwt.exceptions.NotFoundException;
import com.shegami.securityJwt.repositories.ApprovalRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;

    @Override
    public List<Approval> listAllApprovals() {

        return approvalRepository.findAll();

    }

    @Override
    public Approval findByUserUsername(String username) {
        return null;
    }

    @Override
    public Approval getApprovalById(String id) {
        Approval approval = approvalRepository.findById(id).orElse(null);

        if (approval == null) {
            throw new NotFoundException("No approval Found with id: " + id);
        }
        return approval;
    }

    @Override
    public Approval approveRequest(Approval approval) {


        approvalRepository.save(approval);

        return approval;
    }

    @Override
    public void deleteApproval(String id) {
        Approval approval = approvalRepository.findById(id).orElse(null);

        if (approval == null) {
            throw new NotFoundException("No Approval Found with id: " + id);
        }

        approvalRepository.deleteById(id);
    }

    @Override
    public Approval updateApproval(Approval approval) {
        Approval foundApproval = approvalRepository.findById(approval.getId()).orElse(null);

        if (foundApproval == null) {
            throw new NotFoundException("No Approval Found with id: " + approval.getId());
        }

        approvalRepository.save(approval);

        return approval;
    }
}
