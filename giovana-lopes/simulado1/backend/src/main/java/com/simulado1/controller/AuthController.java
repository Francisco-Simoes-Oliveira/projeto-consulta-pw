package com.simulado1.controller;

import com.simulado1.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody(required = false) Map<String, String> credenciais) {
        String username = "aluno@faculdade.edu.br";
        if (credenciais != null && credenciais.containsKey("username")) {
            username = credenciais.get("username");
        }
        String token = jwtUtils.gerarToken(username);
        return ResponseEntity.ok(Map.of(
            "accessToken", token,
            "tokenType", "Bearer",
            "username", username
        ));
    }
}
