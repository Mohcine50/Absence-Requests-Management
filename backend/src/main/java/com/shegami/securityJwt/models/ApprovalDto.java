package com.shegami.securityJwt.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalDto {


    private boolean approved;
    @NotBlank(message = "Please add a comment")
    @NotEmpty(message = "Please add a comment")
    private String comments;

    
}
