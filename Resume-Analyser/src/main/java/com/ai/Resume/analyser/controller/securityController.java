package com.ai.Resume.analyser.controller;


import com.ai.Resume.analyser.model.*;
import com.ai.Resume.analyser.service.securityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("resumeAnalyser/entry/v1")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS, RequestMethod.HEAD})
public class securityController {

    @Autowired
    private securityService service;


    @GetMapping("/health")
    public ResponseEntity<?> health(){
        return ResponseEntity.ok("Auth API is running");
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginInfo(){
        return ResponseEntity.ok("Use POST /resumeAnalyser/entry/v1/login with email, password, role");
    }

    @GetMapping("/register")
    public ResponseEntity<?> registerInfo(){
        return ResponseEntity.ok("Use POST /resumeAnalyser/entry/v1/register after verifyEmail OTP flow");
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody verifyEmailOtp verifyEmail){
        return service.verifyEmail(verifyEmail);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody userRegister req){
        return service.register(req);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody userLogin req){
        return service.login(req);
    }



    @PostMapping("/resetOtpSent")
    public ResponseEntity<?> sentResetOtp(@Valid @RequestBody resetOtp req){
        return service.sentResetOtp(req);
    }
    
    @PostMapping("/verifyResetOtp")
    public ResponseEntity<?> verifyResetOtp(@Valid @RequestBody resetOtpVerification req){
        return service.verifyResetOtp(req);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetAccountPassword(@Valid @RequestBody resetPasscode req){
        return service.resetAccountPassword(req);

    }

}
