package com.financas.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financas.backend.exception.NegocioExcecao;
import com.financas.backend.model.Usuario;
import com.financas.backend.repository.UsuarioRepository;
import com.financas.backend.security.JwtService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Cadastro e login.
 *
 * Pontos de segurança aqui:
 * - validação de entrada com Bean Validation (@Email, @Size, @NotBlank);
 * - senha guardada com BCrypt;
 * - mensagem de erro genérica: não conta se errou o e-mail ou a senha;
 * - a resposta devolve só o token e o nome, nunca a entidade Usuario.
 */
@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/cadastro")
    public RespostaLogin cadastrar(@RequestBody @Valid CadastroRequest requisicao) {
        if (usuarioRepository.existsByEmail(requisicao.getEmail())) {
            throw new NegocioExcecao("E-mail já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(requisicao.getNome().trim());
        usuario.setEmail(requisicao.getEmail().trim().toLowerCase());
        usuario.setSenha(passwordEncoder.encode(requisicao.getSenha()));
        usuarioRepository.save(usuario);

        return new RespostaLogin(jwtService.gerarToken(usuario.getEmail(), usuario.getNome()), usuario.getNome());
    }

    @PostMapping("/login")
    public RespostaLogin login(@RequestBody @Valid LoginRequest requisicao) {
        Usuario usuario = usuarioRepository.findByEmail(requisicao.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new NegocioExcecao("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(requisicao.getSenha(), usuario.getSenha())) {
            throw new NegocioExcecao("E-mail ou senha inválidos");
        }

        return new RespostaLogin(jwtService.gerarToken(usuario.getEmail(), usuario.getNome()), usuario.getNome());
    }

    @Data
    public static class CadastroRequest {

        @NotBlank(message = "Informe o nome")
        @Size(max = 100, message = "Nome muito longo")
        private String nome;

        @NotBlank(message = "Informe o e-mail")
        @Email(message = "E-mail inválido")
        private String email;

        @NotBlank(message = "Informe a senha")
        @Size(min = 8, message = "A senha precisa ter no mínimo 8 caracteres")
        private String senha;
    }

    @Data
    public static class LoginRequest {

        @NotBlank(message = "Informe o e-mail")
        @Email(message = "E-mail inválido")
        private String email;

        @NotBlank(message = "Informe a senha")
        private String senha;
    }

    @Data
    public static class RespostaLogin {

        private final String token;
        private final String nome;
    }
}
