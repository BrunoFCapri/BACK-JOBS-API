package com.uap.proiv.jobs.service.impl;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import com.uap.proiv.jobs.client.JobApiRepository;
import com.uap.proiv.jobs.dto.Job;
import com.uap.proiv.jobs.service.impl.JobServiceImpl;


@ExtendWith(MockitoExtension.class)
public class JobsServicelmplTest {

    @Mock
    JobApiRepository jobApiRepository;

    @InjectMocks
    JobServiceImpl jobServiceImpl;

    List<Job> jobs;

    @BeforeEach
    void setUp() {
        jobs = new ArrayList<>();

        Job job1 = new Job();
        job1.setId(1);
        job1.setName("Developer");
        job1.setSalary(5000);
        job1.setHours(2000);
        job1.setResources(3);

        jobs.add(job1);

        Job job2 = new Job();
        job2.setId(2);
        job2.setName("Designer");
        job2.setSalary(500);
        job2.setHours(200);
        job2.setResources(2);

        jobs.add(job1);
    }
    @Test
    @DisplayName("Verifica el metoso getAllJobs retorne la lista ")
    void getAllJob_Sueccess(){
        when(jobApiRepository.getAllJobs()).thenReturn(jobs);

        List<Job> result = jobServiceImpl.getAllJobs();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Developer", result.get(1).getName());

        verify(jobApiRepository, times(1)).getAllJobs();

    }
    @Test
    @DisplayName("Verifica un expcion de jobapo po rlistado vacio")
    void getAllJob_Exception(){
        when(jobApiRepository.getAllJobs()).thenReturn(new ArrayList<>());

        assertThrows(NoSuchElementException.class, () -> {jobServiceImpl.getJobById(1);});

        verify(jobApiRepository, times(1)).getAllJobs();
    }
    }