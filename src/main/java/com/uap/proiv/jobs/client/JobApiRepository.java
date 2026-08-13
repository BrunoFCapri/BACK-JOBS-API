package com.uap.proiv.jobs.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uap.proiv.jobs.dto.Job;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class JobApiRepository {

    private final ObjectMapper objectMapper;
    private final List<Job> jobs;

    public JobApiRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.jobs = createJobs();
    }


    public List<Job> getAllJobs() {
        return jobs;
    }

    private List<Job> createJobs() {
        try {
            ClassPathResource resource = new ClassPathResource("jobs.json");
            InputStream inputStream = resource.getInputStream();
            return objectMapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Error al leer jobs.json: " + e.getMessage(), e);
        }
    }
}

