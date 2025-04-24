package com.alvaro.sistema.usuariosservicio.controller;

import com.alvaro.sistema.usuariosservicio.security.JwtUtils;
import com.alvaro.sistema.usuariosservicio.security.payload.LoginRequest; // Importa la nueva clase
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Usa ResponseEntity para devolver el token con estado 200
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // Importa UserDetails
import org.springframework.web.bind.annotation.*;

import java.util.List; // Asegurate que List este importado

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager; // Inyecta AuthenticationManager

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) { // Usar @RequestBody y la nueva clase

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(java.util.stream.Collectors.toList());

        String jwt = jwtUtils.generateJwtToken(userDetails.getUsername(), roles); // Pasar roles reales

        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public String register(@RequestBody LoginRequest registerRequest) {
        return "User registered successfully!";
    }
}