package com.financas.backend.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.financas.backend.exception.NaoEncontradoExcecao;
import com.financas.backend.model.Carteira;
import com.financas.backend.service.CarteiraService;

/**
 * Teste de INTEGRAÇÃO da camada web: sobe só o controller (não o banco).
 *
 * O MockMvc faz a requisição HTTP "de mentira" e a gente confere status e JSON.
 * O service é dublê (@MockitoBean), porque aqui quem está sendo testado é a rota.
 */
@WebMvcTest(CarteiraController.class)
class CarteiraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarteiraService carteiraService;

    @Test
    @DisplayName("GET /api/carteira devolve 200 e a lista do usuário")
    @WithMockUser(username = "joao@exemplo.com") // finge que tem alguém logado
    void deveListarCarteiras() throws Exception {
        Carteira carteira = new Carteira();
        carteira.setId(1L);
        carteira.setNome("Casa");

        when(carteiraService.listarMinhas()).thenReturn(List.of(carteira));

        mockMvc.perform(get("/api/carteira"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Casa"));
    }

    @Test
    @DisplayName("GET /api/carteira/{id} de outro usuário devolve 404")
    @WithMockUser(username = "joao@exemplo.com")
    void deveDevolver404QuandoCarteiraNaoEhDoUsuario() throws Exception {
        when(carteiraService.buscarPorId(eq(99L)))
                .thenThrow(new NaoEncontradoExcecao("Carteira não encontrada"));

        mockMvc.perform(get("/api/carteira/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Sem token, a rota protegida devolve 401")
    void deveExigirAutenticacao() throws Exception {
        mockMvc.perform(get("/api/carteira"))
                .andExpect(status().isUnauthorized());
    }
}
