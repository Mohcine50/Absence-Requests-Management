package com.shegami.securityJwt.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AbsenceDto {

    @NotBlank(message = "Title must be included")
    @NotNull(message = "Title is empty")
    private String title;
    @NotBlank(message = "StartDate must be included")
    @NotNull(message = "StartDate is empty")
    private String startDate;
    @NotBlank(message = "EndDate must be included")
    @NotNull(message = "EndDate is empty")
    private String endDate;
    @NotBlank(message = "Please add a reason")
    @NotNull(message = "Please add a reason")
    private String reason;

}
