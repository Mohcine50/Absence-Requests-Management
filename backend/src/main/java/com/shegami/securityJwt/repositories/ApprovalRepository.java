package com.shegami.securityJwt.repositories;

import com.shegami.securityJwt.entities.Approval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRepository extends JpaRepository<Approval, String> {
}
