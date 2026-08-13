package com.uap.proiv.jobs.service.impl;

import com.uap.proiv.jobs.dto.AssignedResponse;
import com.uap.proiv.jobs.dto.Job;
import com.uap.proiv.jobs.service.AssignedService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AssignedServiceImpl implements AssignedService {

    @Override
    public List<AssignedResponse> create(List<Job> jobIds, List<Integer> userIds) {
        List<AssignedResponse> responseList = new ArrayList<>();
        jobIds.forEach(job -> {
            Collections.shuffle(userIds);
            for (int i=0; i<job.getResources(); i++){
                responseList.add(new AssignedResponse(job.getId(), userIds.get(i)));
            }
        });
        return responseList;
    }
}
