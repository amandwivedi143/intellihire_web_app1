package com.ai.Resume.analyser.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobRequest {

    @NotBlank(message = "Company name must not be empty")
    private String companyName;

    @NotBlank(message = "Job title must not be empty")
    private String title;

    @NotBlank(message = "Description must not be empty")
    private String description;

    @NotBlank(message = "Location must not be empty")
    private String location;
}
