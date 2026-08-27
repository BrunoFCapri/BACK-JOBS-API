package com.uap.proiv.jobs.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.uap.proiv.jobs.client.UserApiRepository;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired  
    private MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public UserApiRepository userApiRepository;

    static MockWebServer mockWebServer;

    @BeforeAll
    public static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll 
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean 
        @Primary
        public UserApiRepository userApiRepository(ObjectMapper objectMapper) {
            HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
            String baseUrl = mockWebServer.url("/api/users").toString();
            String apiKey = "free_user_3HYiqu2JKQ4TfGq884xW5mqfrd";

            // REVISAR: Asegurate que el constructor de UserApiRepository reciba exactamente estos tipos.
            return new UserApiRepository(httpClient, objectMapper, baseUrl, apiKey);
        }
    }

    @Test
    @DisplayName("GET api.users/{id} integracion UserController, UserService, UserRepository, mock api externa")
    void getUserById() throws Exception {
        String expectedResponse = """
        {
            "id": 2,
            "email": "juan@gmail.com",
            "first_name": "Juan",
            "last_name": "Perez",
            "avatar": "https://reqres.in/img/faces/2-image.jpg"
        }
        """;
        
        mockWebServer.enqueue(new MockResponse()
            .setBody(expectedResponse)
            .setResponseCode(200)
            .addHeader("Content-Type", "application/json"));    

        mockMvc.perform(get("/api/user/id/2"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.email").value("juan@gmail.com"))
            .andExpect(jsonPath("$.first_name").value("Juan"))
            .andExpect(jsonPath("$.last_name").value("Perez"))
            .andExpect(jsonPath("$.avatar").value("https://reqres.in/img/faces/2-image.jpg"));
    
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("application/json", recordedRequest.getHeader("Accept"));
        assertEquals("free_user_3HYiqu2JKQ4TfGq884xW5mqfrd", recordedRequest.getHeader("X-API-Key"));
    }
@Test
    @DisplayName(" POST /api/user/update integracion UserController, UserService, UserRepository, mock api externa")
    void ipdateUser() throws Exception {
        String updateResponse = """
        {
            "id": 2,
            "email": "juanperez@gmail.com",
            "first_name": "Juan",
            "last_name": "Perez",
            "avatar": "https://reqres.in/img/faces/2-image.jpg"
        }
        """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(updateResponse)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));    

        String userJson = """
            {
                "id": 2,
                "email": "juanperez@gmail.com",
                "first_name": "Juan",
                "last_name": "Perez",
                "avatar": "https://reqres.in/img/faces/2-image.jpg"
            }
            """;

        mockMvc.perform(post("/api/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("juanperez@gmail.com"));

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("application/json", recordedRequest.getHeader("Content-Type"));  
        assertEquals("free_user_3HYiqu2JKQ4TfGq884xW5mqfrd", recordedRequest.getHeader("X-API-Key"));  
    }
}
    