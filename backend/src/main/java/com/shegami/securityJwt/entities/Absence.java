package com.shegami.securityJwt.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "absences")
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @NotNull
    private String title;

    @Column(nullable = false)
    @NotNull
    private String startDate;

    @Column(nullable = false)
    @NotNull
    private String endDate;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private String createdDate;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "absence", cascade = CascadeType.ALL)
    private Approval approval;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "absence")
    private Notification notification;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;


}





















