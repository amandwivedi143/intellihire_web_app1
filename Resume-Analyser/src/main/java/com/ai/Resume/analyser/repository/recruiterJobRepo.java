package com.ai.Resume.analyser.repository;

import com.ai.Resume.analyser.model.RecruiterJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface recruiterJobRepo extends JpaRepository<RecruiterJob, Long> {
    List<RecruiterJob> findByRecruiterEmailOrderByCreatedAtDesc(String recruiterEmail);
    List<RecruiterJob> findAllByOrderByCreatedAtDesc();
}
