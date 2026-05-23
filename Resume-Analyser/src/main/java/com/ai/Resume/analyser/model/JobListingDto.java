package com.ai.Resume.analyser.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobListingDto {
    private Long id;
    private String companyName;
    private String title;
    private String description;
    private String location;
    private String recruiterName;
    private Date createdAt;
    private boolean applied;
}
