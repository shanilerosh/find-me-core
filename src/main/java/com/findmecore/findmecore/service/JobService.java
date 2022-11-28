package com.findmecore.findmecore.service;

import com.findmecore.findmecore.dto.FriendCommonDto;
import com.findmecore.findmecore.dto.JobDto;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface JobService {


    Boolean createJob(String employerId, JobDto jobDto);

    List<JobDto> fetchJobsByEmployer(String employerId);

    Boolean updateJob(String employerId, JobDto jobDto);

    List<JobDto> fetchAllJobs();

    Boolean applyJob(String empId, String jobId);

    List<FriendCommonDto> findEmployeesByJob(String jobId);
}
