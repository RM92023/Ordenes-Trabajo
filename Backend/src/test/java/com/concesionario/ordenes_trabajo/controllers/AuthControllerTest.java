package com.concesionario.ordenes_trabajo.controllers;

import com.concesionario.ordenes_trabajo.config.JwtUtil;
import com.concesionario.ordenes_trabajo.dtos.LoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.mockito.Mockito.*;

import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;
    private AuthenticationManager authManager;
    private JwtUtil jwtUtil;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        authManager = mock(AuthenticationManager.class);
        jwtUtil = mock(JwtUtil.class);
        AuthController authController = new AuthController(authManager, jwtUtil);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void login_shouldReturnToken() throws Exception {
        LoginDTO login = new LoginDTO();
        login.setUsername("admin");
        login.setPassword("admin123");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        when(jwtUtil.generateToken("admin")).thenReturn("mocked-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(content().string("Bearer mocked-token"));
    }
}
