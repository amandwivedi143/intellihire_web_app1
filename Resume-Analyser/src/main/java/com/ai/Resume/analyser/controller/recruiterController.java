package com.ai.Resume.analyser.controller;

import com.ai.Resume.analyser.model.CreateJobRequest;
import com.ai.Resume.analyser.service.recruiterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("resumeAnalyserCore/service/v1")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS, RequestMethod.HEAD})
public class recruiterController {

    @Autowired
    private recruiterService recruiterServices;

    @PostMapping("/recruiter/jobs")
    public ResponseEntity<?> postJob(@Valid @RequestBody CreateJobRequest req){
        return recruiterServices.postJob(req);
    }

    @GetMapping("/recruiter/jobs")
    public ResponseEntity<?> getRecruiterJobs(){
        return recruiterServices.getRecruiterJobs();
    }

    @GetMapping("/jobs")
    public ResponseEntity<?> getPostedJobsForSeeker(){
        return recruiterServices.getPostedJobsForSeeker();
    }

    @PostMapping("/jobs/{jobId}/apply")
    public ResponseEntity<?> applyForJob(@PathVariable Long jobId){
        return recruiterServices.applyToJob(jobId);
    }
}
