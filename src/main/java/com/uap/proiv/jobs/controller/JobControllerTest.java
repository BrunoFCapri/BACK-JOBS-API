package com.uap.proiv.jobs.controller;

import java.util.List;
import java.util.ArrayList;
import com.uap.proiv.jobs.dto.Job;
import com.uap.proiv.jobs.dto.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

import com.uap.proiv.jobs.dto.UserApiResponse;
import com.uap.proiv.jobs.service.JobService;
import com.uap.proiv.jobs.service.UserJobAssignedService;
import com.uap.proiv.jobs.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;



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
    private List<User> users;
    private List<Job> jobs;



    @BeforeEach
    void setup(){
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

        users = new ArrayList<>();
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user1@ejemplo.com");
        user1.setAvatar("null");
        user1.setFirstName("juan");
        user1.setLastName("nuñez");
        users.add(user1);

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("user1@ejemplo.com");
        user2.setAvatar("null");
        user2.setFirstName("juan");
        user2.setLastName("nuñez");
        users.add(user2);

        userApiResponse = new UserApiResponse();
        userApiResponse.setPage(1);
        userApiResponse.setPerPage(2);
        userApiResponse.setTotal(2);
        userApiResponse.setTotalPages(1);
        userApiResponse.setData(users);

    }

    @Test
    @DisplayName("GET /api/job/users/{page} retona usuarios")
    void getUsers_seccess() throws Exception{
        when(userService.search(1)).thenReturn(userApiResponse);

        mockMvc.perform(get("/api/job/users/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.page").value(1))
            .andExpect(jsonPath("$.total").value(2));

    }

    @Test
    @DisplayName("GET /api/job/users/{page} - Exepcion retornada por el servidor")
    void  getUsers_exception() throws Exception{
        when(userService.search(2)).thenThrow(new RuntimeException("Service Error"));

        mockMvc.perform(get("/api/job/users/2"))
            .andExpect(status().is5xxServerError())
            .andExpect(content().string("Service Error"));
    }

    @Test
    @DisplayName("POST /api/job/assign")
    void postAssign_success() throws Exception{

    }
}
