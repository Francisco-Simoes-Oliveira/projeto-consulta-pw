package com.financas.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financas.backend.model.Carteira;
import com.financas.backend.service.CarteiraService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Mesmo controller de sempre, agora com as anotações do Swagger.
 *
 * @Tag        -> agrupa as rotas na tela
 * @Operation  -> título e descrição da rota
 * @ApiResponses -> quais status essa rota pode devolver
 * @Parameter  -> explica um parâmetro
 */
@Tag(name = "Carteiras", description = "Criação e gestão das carteiras do usuário")
@RestController
@RequestMapping("/api/carteira")
public class ExemploControllerDocumentado {

    @Autowired
    private CarteiraService carteiraService;

    @Operation(summary = "Lista as carteiras do usuário logado",
            description = "Retorna apenas as carteiras em que o usuário autenticado é dono ou membro.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido")
    })
    @GetMapping
    public List<Carteira> listar() {
        return carteiraService.listarMinhas();
    }

    @Operation(summary = "Busca uma carteira pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carteira encontrada"),
            @ApiResponse(responseCode = "404", description = "Carteira inexistente ou de outro usuário")
    })
    @GetMapping("/{id}")
    public Carteira buscar(
            @Parameter(description = "Id da carteira", example = "1") @PathVariable Long id) {
        return carteiraService.buscarPorId(id);
    }

    @Operation(summary = "Cria uma carteira")
    @ApiResponse(responseCode = "201", description = "Carteira criada")
    @PostMapping
    public ResponseEntity<Carteira> criar(@RequestBody @Valid Carteira carteira) {
        return new ResponseEntity<>(carteiraService.criar(carteira), HttpStatus.CREATED);
    }

    @Operation(summary = "Exclui uma carteira")
    @ApiResponse(responseCode = "204", description = "Carteira excluída")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        carteiraService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
