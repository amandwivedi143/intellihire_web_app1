package com.ai.Resume.analyser.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long jobId;
    private String seekerEmail;
    private String seekerName;
    private Integer resumeScore;
    private Integer atsScore;
    private String analysedRole;

    @CreationTimestamp
    @Column(updatable = false)
    private Date appliedAt;
}
