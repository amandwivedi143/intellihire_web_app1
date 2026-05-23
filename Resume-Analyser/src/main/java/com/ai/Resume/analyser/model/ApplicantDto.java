package com.ai.Resume.analyser.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantDto {
    private String seekerName;
    private String seekerEmail;
    private Integer resumeScore;
    private Integer atsScore;
    private String analysedRole;
    private Date appliedAt;
}
