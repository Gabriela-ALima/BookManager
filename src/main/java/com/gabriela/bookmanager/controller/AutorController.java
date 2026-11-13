package com.gabriela.bookmanager.controller;

import com.gabriela.bookmanager.dto.AutorRequest;
import com.gabriela.bookmanager.dto.LivroResponse;
import com.gabriela.bookmanager.model.Autor;
import com.gabriela.bookmanager.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
@Tag(name = "Autores", description = "Cadastro e consulta de autores")
public class AutorController {

    private final AutorService autorService;

    @GetMapping
    @Operation(summary = "Listar autores", description = "Retorna todos os autores cadastrados.")
    public List<Autor> listar() {
        return autorService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar autor por ID")
    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    public Autor buscar(@Parameter(description = "ID do autor", example = "1") @PathVariable Long id) {
        return autorService.buscarPorId(id);
    }

    @GetMapping("/{id}/livros")
    @Operation(summary = "Listar livros de um autor",
            description = "Retorna os livros associados ao autor informado.")
    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    public List<LivroResponse> listarLivros(@Parameter(description = "ID do autor", example = "1") @PathVariable Long id) {
        return autorService.listarLivros(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar autor")
    @ApiResponse(responseCode = "400", description = "Dados inválidos (ex.: nome em branco)")
    public Autor criar(@Valid @RequestBody AutorRequest req) {
        return autorService.criar(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar autor")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    public Autor atualizar(@Parameter(description = "ID do autor", example = "1") @PathVariable Long id,
                           @Valid @RequestBody AutorRequest req) {
        return autorService.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir autor",
            description = "Não é possível excluir um autor que possui livros cadastrados.")
    @ApiResponse(responseCode = "400", description = "O autor possui livros cadastrados")
    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    public void excluir(@Parameter(description = "ID do autor", example = "1") @PathVariable Long id) {
        autorService.excluir(id);
    }
}