package com.uap.proiv.jobs.service;

import com.uap.proiv.jobs.dto.Job;

import java.util.List;

public interface JobService {
    List<Job> getAllJobs();
    Job getJobById(int id);
}
