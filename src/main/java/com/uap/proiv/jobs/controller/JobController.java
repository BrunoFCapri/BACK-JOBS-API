package com.uap.proiv.jobs.controller;

import com.uap.proiv.jobs.dto.AssignRequest;
import com.uap.proiv.jobs.dto.UserJobAssigned;
import com.uap.proiv.jobs.service.JobService;
import com.uap.proiv.jobs.service.UserJobAssignedService;
import com.uap.proiv.jobs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/job")
public class JobController {

    private final UserService userService;
    private final UserJobAssignedService userJobAssignedService;
    private final JobService jobService;

    @Autowired
    public JobController(UserService userService,
                         UserJobAssignedService userJobAssignedService,
                         JobService jobService) {
        this.userService = userService;
        this.userJobAssignedService = userJobAssignedService;
        this.jobService = jobService;
    }

    @GetMapping("/users/{page}")
    public ResponseEntity<Object> getAllUsersApi(@PathVariable Integer page) {
        try {
            return ResponseEntity.ok(userService.search(page));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(userService.searchById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllJobs() {
        try {
            return ResponseEntity.ok(jobService.getAllJobs());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getJobById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(jobService.getJobById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/assign")
    public ResponseEntity<Object> assign(@RequestBody  @Valid AssignRequest request) {
        try {
            List<UserJobAssigned> list = userJobAssignedService.assign();
            Map<String, Object> response = new HashMap<>();
            response.put("Client", request.getClientName());
            response.put("Request_Number", request.getRequestNumber());
            response.put("Assign", list);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
