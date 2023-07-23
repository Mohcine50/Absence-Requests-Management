package com.shegami.securityJwt.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class NotificationDto {


    @NotBlank(message = "date is Missing")
    @NotEmpty(message = "date is Missing")
    private String date;
    @NotBlank(message = "Please add a content")
    @NotEmpty(message = "Please add a content")
    private String content;
}
