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
public class RecruiterJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recruiterEmail;
    private String recruiterName;
    private String companyName;
    private String title;

    @Column(length = 3000)
    private String description;

    private String location;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;
}
