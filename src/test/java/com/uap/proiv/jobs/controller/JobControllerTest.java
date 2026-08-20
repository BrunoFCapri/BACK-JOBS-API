package com.uap.proiv.jobs.controller;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.uap.proiv.jobs.service.UserService;
import com.uap.proiv.jobs.dto.Job;
import com.uap.proiv.jobs.dto.UserApiResponse;
import com.uap.proiv.jobs.service.JobService;
import com.uap.proiv.jobs.service.UserJobAssignedService;


@ExtendWith(MockitoExtension.class)
public class JobControllerTest {

    @Mock 
    UserService userService;

    @Mock
    JobService jobService;

    @Mock
    UserJobAssignedService userJobAssignedService;

    @InjectMocks
    JobController jobController;

    private MockMvc mockMvc; 

    private UserApiResponse userApiResponse;
    private List<Job> jobs;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();

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
        jobs.add(job2);

    }
    @Test
    @DisplayName("GET API /api/job/users/{page} - Not Found / Empty")
    void getAllUsersApi_Empty() throws Exception {
    
        when(userService.search(1)).thenReturn(new UserApiResponse());

        mockMvc.perform(get("/api/job/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }
}


