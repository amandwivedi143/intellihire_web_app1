package com.ai.Resume.analyser.service;

import com.ai.Resume.analyser.model.*;
import com.ai.Resume.analyser.repository.jobApplicationRepo;
import com.ai.Resume.analyser.repository.prevTable;
import com.ai.Resume.analyser.repository.recruiterJobRepo;
import com.ai.Resume.analyser.repository.usersTableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class recruiterService {

    @Autowired
    private usersTableRepo usersTableRepository;

    @Autowired
    private recruiterJobRepo recruiterJobRepository;

    @Autowired
    private jobApplicationRepo jobApplicationRepository;

    @Autowired
    private prevTable previousTableRepo;

    private usersTable getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null || email.isBlank()) {
            return null;
        }
        return usersTableRepository.findById(email).orElse(null);
    }

    private boolean isRecruiter(usersTable user) {
        return user != null && "JOB_RECRUITER".equals(user.getRole());
    }

    private boolean isSeeker(usersTable user) {
        return user != null && "JOB_SEEKER".equals(user.getRole());
    }

    public ResponseEntity<?> postJob(CreateJobRequest req) {
        usersTable user = getCurrentUser();
        if (!isRecruiter(user)) {
            return new ResponseEntity<>("Only recruiters can post jobs", HttpStatus.FORBIDDEN);
        }

        RecruiterJob job = new RecruiterJob();
        job.setRecruiterEmail(user.getEmail());
        job.setRecruiterName(user.getUsername());
        job.setCompanyName(req.getCompanyName().trim());
        job.setTitle(req.getTitle().trim());
        job.setDescription(req.getDescription().trim());
        job.setLocation(req.getLocation().trim());
        recruiterJobRepository.save(job);
        return new ResponseEntity<>("Job posted successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<?> getRecruiterJobs() {
        usersTable user = getCurrentUser();
        if (!isRecruiter(user)) {
            return new ResponseEntity<>("Only recruiters can access this data", HttpStatus.FORBIDDEN);
        }

        List<RecruiterJob> jobs = recruiterJobRepository.findByRecruiterEmailOrderByCreatedAtDesc(user.getEmail());
        List<RecruiterJobDto> response = new ArrayList<>();
        for (RecruiterJob job : jobs) {
            List<JobApplication> applications = jobApplicationRepository.findByJobIdOrderByAppliedAtDesc(job.getId());
            List<ApplicantDto> applicants = applications.stream()
                    .map(app -> new ApplicantDto(
                            app.getSeekerName(),
                            app.getSeekerEmail(),
                            app.getResumeScore(),
                            app.getAtsScore(),
                            app.getAnalysedRole(),
                            app.getAppliedAt()
                    )).toList();

            response.add(new RecruiterJobDto(
                    job.getId(),
                    job.getCompanyName(),
                    job.getTitle(),
                    job.getDescription(),
                    job.getLocation(),
                    job.getCreatedAt(),
                    applicants
            ));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> getPostedJobsForSeeker() {
        usersTable user = getCurrentUser();
        if (!isSeeker(user)) {
            return new ResponseEntity<>("Only job seekers can access posted jobs", HttpStatus.FORBIDDEN);
        }

        List<RecruiterJob> jobs = recruiterJobRepository.findAllByOrderByCreatedAtDesc();
        List<JobListingDto> response = jobs.stream().map(job -> new JobListingDto(
                job.getId(),
                job.getCompanyName(),
                job.getTitle(),
                job.getDescription(),
                job.getLocation(),
                job.getRecruiterName(),
                job.getCreatedAt(),
                jobApplicationRepository.existsByJobIdAndSeekerEmail(job.getId(), user.getEmail())
        )).toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> applyToJob(Long jobId) {
        usersTable user = getCurrentUser();
        if (!isSeeker(user)) {
            return new ResponseEntity<>("Only job seekers can apply jobs", HttpStatus.FORBIDDEN);
        }
        if (jobId == null) {
            return new ResponseEntity<>("Job not found", HttpStatus.NOT_FOUND);
        }

        RecruiterJob job = recruiterJobRepository.findById(jobId).orElse(null);
        if (job == null) {
            return new ResponseEntity<>("Job not found", HttpStatus.NOT_FOUND);
        }
        String seekerEmail = user.getEmail();
        if (seekerEmail == null || seekerEmail.isBlank()) {
            return new ResponseEntity<>("Unauthorised request", HttpStatus.UNAUTHORIZED);
        }
        if (jobApplicationRepository.existsByJobIdAndSeekerEmail(jobId, seekerEmail)) {
            return new ResponseEntity<>("Already applied for this job", HttpStatus.CONFLICT);
        }

        previousTable latestResume = previousTableRepo.findById(seekerEmail).orElse(null);
        JobApplication application = new JobApplication();
        application.setJobId(jobId);
        application.setSeekerEmail(seekerEmail);
        application.setSeekerName(user.getUsername());
        if (latestResume != null) {
            application.setResumeScore(latestResume.getScore());
            application.setAtsScore(latestResume.getAtsoptimizationscore());
            application.setAnalysedRole(latestResume.getRoles());
        }
        jobApplicationRepository.save(application);
        return new ResponseEntity<>("Applied successfully", HttpStatus.OK);
    }
}
