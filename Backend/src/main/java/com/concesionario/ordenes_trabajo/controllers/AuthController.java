package com.concesionario.ordenes_trabajo.controllers;

import com.concesionario.ordenes_trabajo.config.JwtUtil;
import com.concesionario.ordenes_trabajo.dtos.LoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        var auth = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        authManager.authenticate(auth);

        String token = jwtUtil.generateToken(request.getUsername());
        return ResponseEntity.ok().body("Bearer " + token);
    }
}
