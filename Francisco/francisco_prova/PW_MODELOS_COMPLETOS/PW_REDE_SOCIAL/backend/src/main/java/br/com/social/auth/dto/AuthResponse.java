package br.com.social.auth.dto;

public record AuthResponse(String token, String name, String email, String role) {}
