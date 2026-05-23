package com.ai.Resume.analyser.repository;

import com.ai.Resume.analyser.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface jobApplicationRepo extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJobIdOrderByAppliedAtDesc(Long jobId);
    boolean existsByJobIdAndSeekerEmail(Long jobId, String seekerEmail);
}
