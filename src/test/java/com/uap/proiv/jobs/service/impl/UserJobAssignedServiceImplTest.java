package com.uap.proiv.jobs.service.impl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.uap.proiv.jobs.dto.AssignedResponse;
import com.uap.proiv.jobs.dto.Job;
import com.uap.proiv.jobs.dto.User;
import com.uap.proiv.jobs.dto.UserApiResponse;
import com.uap.proiv.jobs.dto.UserJobAssigned;
import com.uap.proiv.jobs.service.AssignedService;
import com.uap.proiv.jobs.service.JobService;
import com.uap.proiv.jobs.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserJobAssignedServiceImplTest {

    @Mock
    JobService jobService;

    @Mock
    UserService userService;

    @Mock
    AssignedService assignmentService;

    @InjectMocks
    UserJobAssignedServiceImpl serviceImpl;

    List<Job> jobs;
    List<User> users;
    List<AssignedResponse> assignments;
    UserApiResponse userApiResponse;

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
        jobs.add(job2);

        users = new ArrayList<>();
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user1@example.com");
        user1.setFirstName("User");
        user1.setAvatar(null);
        user1.setLastName("One");
        users.add(user1);

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("user2@example.com");
        user2.setFirstName("User");
        user2.setAvatar(null);
        user2.setLastName("Two");
        users.add(user2);

        assignments = new ArrayList<>();
        assignments.add(new AssignedResponse(1, 1));
        assignments.add(new AssignedResponse(2, 2));

        // assign() hace new ArrayList<>(userApiResponse.getData()), asi que data no puede quedar en null
        userApiResponse = new UserApiResponse();
        userApiResponse.setPage(1);
        userApiResponse.setPerPage(2);
        userApiResponse.setTotal(2);
        userApiResponse.setTotalPages(1);
        userApiResponse.setData(users);
    }

    @Test
    @DisplayName("Verifica que el método assign retorne la lista de asignaciones correctamente")
    void assign_successOnAPage() {
        when(jobService.getAllJobs()).thenReturn(jobs);
        when(userService.search(1)).thenReturn(userApiResponse);
        // el bucle de paginado sigue pidiendo la pagina siguiente hasta recibir null
        when(userService.search(2)).thenReturn(null);
        when(assignmentService.create(jobs, List.of(1, 2))).thenReturn(assignments);

        List<UserJobAssigned> result = serviceImpl.assign();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Developer", result.get(0).getJob().getName());
        assertEquals(1, result.get(0).getUsers().size());
        assertEquals(1, result.get(0).getUsers().get(0).getId());

        assertEquals("Designer", result.get(1).getJob().getName());
        assertEquals(1, result.get(1).getUsers().size());
        assertEquals(2, result.get(1).getUsers().get(0).getId());

        verify(jobService, times(1)).getAllJobs();
        verify(userService, times(1)).search(1);
        verify(userService, times(1)).search(2);
        verify(assignmentService, times(1)).create(jobs, List.of(1, 2));
    }
}
