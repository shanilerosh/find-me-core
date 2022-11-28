package com.findmecore.findmecore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.findmecore.findmecore.dto.FriendCommonDto;
import com.findmecore.findmecore.dto.JobDto;
import com.findmecore.findmecore.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ShanilErosh
 */
@Slf4j
@RestController
@RequestMapping("/api/job")
public class JobController {


    @Autowired
    private JobService jobService;


    @PostMapping(value = "/{employerId}")
    public ResponseEntity<Boolean> createJob(@PathVariable String employerId, @RequestBody JobDto jobDto) {
        return ResponseEntity.ok(jobService.createJob(employerId,jobDto));
    }

    @PutMapping(value = "/{employerId}")
    public ResponseEntity<Boolean> updateJob(@PathVariable String employerId, @RequestBody JobDto jobDto) {
        return ResponseEntity.ok(jobService.updateJob(employerId,jobDto));
    }

    @GetMapping(value = "/{employerId}")
    public ResponseEntity<List<JobDto>> fetchJobsByEmployer(@PathVariable String employerId) {
        return ResponseEntity.ok(jobService.fetchJobsByEmployer(employerId));
    }


    @GetMapping(value = "/all")
    public ResponseEntity<List<JobDto>> fetchAllJobs() {
        return ResponseEntity.ok(jobService.fetchAllJobs());
    }

    @GetMapping(value = "/apply/{empId}/{jobId}")
    public ResponseEntity<Boolean> applyJob(@PathVariable String empId, @PathVariable String jobId) {
        return ResponseEntity.ok(jobService.applyJob(empId, jobId));
    }

    @GetMapping("/employee-applied/{jobId}")
    public ResponseEntity<List<FriendCommonDto>> findEmployeesByJob(@PathVariable String jobId) {
        return ResponseEntity.ok(jobService.findEmployeesByJob(jobId));
    }

    }
