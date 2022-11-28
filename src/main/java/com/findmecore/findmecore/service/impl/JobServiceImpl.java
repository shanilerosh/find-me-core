package com.findmecore.findmecore.service.impl;

import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.Employer;
import com.findmecore.findmecore.entity.Job;
import com.findmecore.findmecore.entity.JobEmployee;
import com.findmecore.findmecore.repo.EmployeeRepository;
import com.findmecore.findmecore.repo.EmployerRepository;
import com.findmecore.findmecore.repo.JobEmployeeRepository;
import com.findmecore.findmecore.repo.JobRepository;
import com.findmecore.findmecore.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ShanilErosh
 */
@Service
public class JobServiceImpl implements JobService {


    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobEmployeeRepository jobEmployeeRepository;

    @Override
    public Boolean createJob(String employerId, JobDto jobDto) {

        Employer employer = findEmployerById(employerId);

        Job job = Job.builder().createdDate(LocalDateTime.now())
                .expiryDate(jobDto.getExpiryDate())
                .jobTitle(jobDto.getTitle())
                .jobDescription(jobDto.getContent())
                .jobType(jobDto.getType())
                .status("ACTIVE")
                .employer(employer).build();

        jobRepository.save(job);

        return true;
    }

    @Override
    public List<JobDto> fetchJobsByEmployer(String employerId) {
        Employer employer = findEmployerById(employerId);

        return jobRepository.
                findAllByEmployerAndStatus(employer, "ACTIVE")
                .stream().map(this::covertJobToJobDto).collect(Collectors.toList());


    }

    @Override
    public Boolean updateJob(String jobId, JobDto jobDto) {

        Job job = findJobById(jobId);

        job.setCreatedDate(LocalDateTime.now());
        job.setJobDescription(jobDto.getContent());
        job.setJobType(jobDto.getType());
        job.setJobTitle(jobDto.getTitle());
        job.setExpiryDate(jobDto.getExpiryDate());

        jobRepository.save(job);
        return true;
    }

    private Job findJobById(String jobId) {
        return jobRepository.findById(Long.valueOf(jobId))
                .orElseThrow(() -> {
                    throw new RuntimeException("Job not found");
                });
    }

    @Override
    public List<JobDto> fetchAllJobs() {
        return jobRepository.findAllByStatus("ACTIVE")
                .stream().map(this::covertJobToJobDto).collect(Collectors.toList());
    }

    @Override
    public Boolean applyJob(String empId, String jobId) {

        Employee appliedEmployee = findEmployeeById(empId);
        Job appliedJob = findJobById(jobId);


        //find for duplicated
        jobEmployeeRepository.findByEmployeeAndJob(appliedEmployee, appliedJob)
                .ifPresent(c -> {
                    throw new RuntimeException("You have already appliced for this job "+ getDays(c.getAppliedDate()));
                });

        JobEmployee jobEmployee = JobEmployee.builder().employee(appliedEmployee)
                .job(appliedJob).appliedDate(LocalDateTime.now())
                .build();

        jobEmployeeRepository.save(jobEmployee);

        return true;
    }

    private Employee findEmployeeById(String empId) {
        return employeeRepository.findById(Long.valueOf(empId))
                .orElseThrow(() -> {
                    throw new ResolutionException("Employee not found");
                });
    }


    private String getDays(LocalDateTime created) {
        long seconds = ChronoUnit.SECONDS.between(created, LocalDateTime.now());
        if(seconds < 60) {
            return seconds+" seconds ago";
        }
        long mins = ChronoUnit.MINUTES.between(created, LocalDateTime.now());

        if(mins < 60) {
            return mins+" minutes ago";
        }
        long hours = ChronoUnit.HOURS.between(created, LocalDateTime.now());
        if(hours < 24) {
            return hours+" minutes ago";
        }

        long days = ChronoUnit.DAYS.between(created, LocalDateTime.now());

        return days+" days ago";
    }


    private JobDto covertJobToJobDto(Job job) {
        return JobDto.builder().
                employerName(job.getEmployer().getName())
                .jobId(job.getId())
                .employerPic(job.getEmployer().getProfilePicLocation())
                .content(job.getJobDescription())
                .title(job.getJobTitle())
                .type(job.getJobType())
                .expiryDate(job.getExpiryDate())
                .createdOn(getDays(job.getCreatedDate()))
                .build();
    }

    private Employer findEmployerById(String employerId) {
        return employerRepository.findById(Long.valueOf(employerId))
                .orElseThrow(() -> {
                    throw new RuntimeException("Employer not found");
                });
    }
}
