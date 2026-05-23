package com.ai.Resume.analyser.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruiterJobDto {
    private Long id;
    private String companyName;
    private String title;
    private String description;
    private String location;
    private Date createdAt;
    private List<ApplicantDto> applicants;
}
