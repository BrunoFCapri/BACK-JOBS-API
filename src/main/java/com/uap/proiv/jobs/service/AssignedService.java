package com.uap.proiv.jobs.service;

import com.uap.proiv.jobs.dto.AssignedResponse;
import com.uap.proiv.jobs.dto.Job;

import java.util.List;

public interface AssignedService {
    List<AssignedResponse> create(List<Job> jobs, List<Integer> userIds);
}
