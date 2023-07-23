package com.shegami.securityJwt.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shegami.securityJwt.models.ApprovalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "approvals")
public class Approval {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private ApprovalStatus approved;

    @Column(nullable = false)
    @NotBlank
    private String comments;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "absence_id", nullable = false)
    private Absence absence;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", nullable = false)
    private AppUser manager;
}
