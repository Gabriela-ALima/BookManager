package com.gabriela.bookmanager.controller;

import com.gabriela.bookmanager.dto.LivroRequest;
import com.gabriela.bookmanager.dto.LivroResponse;
import com.gabriela.bookmanager.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
@Tag(name = "Livros", description = "Cadastro, pesquisa e exportação de livros")
public class LivroController {

    private final LivroService livroService;

    @GetMapping
    @Operation(summary = "Listar e pesquisar livros",
            description = "Sem filtros, retorna todos os livros. Os filtros fazem busca parcial, sem diferenciar "
                    + "maiúsculas de minúsculas. Se mais de um filtro for enviado, vale o primeiro, nesta ordem: "
                    + "título, autor, gênero.")
    public List<LivroResponse> listar(
            @Parameter(description = "Parte do título", example = "dom") @RequestParam(required = false) String titulo,
            @Parameter(description = "Parte do nome do autor", example = "machado") @RequestParam(required = false) String autor,
            @Parameter(description = "Parte do gênero", example = "romance") @RequestParam(required = false) String genero) {
        return livroService.listar(titulo, autor, genero);
    }

    @GetMapping("/exportar")
    @Operation(summary = "Exportar livros em JSON",
            description = "Gera o arquivo livros.json com todos os livros cadastrados (título, ISBN, ano, gênero, "
                    + "quantidades e nome do autor). Para baixar o arquivo, abra esta URL diretamente no navegador.")
    public ResponseEntity<List<LivroResponse>> exportar() {
        List<LivroResponse> livros = livroService.listar(null, null, null);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"livros.json\"")
                .contentType(MediaType.APPLICATION_JSON)
                .body(livros);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    public LivroResponse buscar(@Parameter(description = "ID do livro", example = "1") @PathVariable Long id) {
        return livroService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar livro",
            description = "O autor precisa estar cadastrado. No cadastro, a quantidade disponível é igual à quantidade total.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    public LivroResponse criar(@Valid @RequestBody LivroRequest req) {
        return livroService.criar(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro",
            description = "Se a quantidade total mudar, a diferença é aplicada à quantidade disponível. "
                    + "Não é possível reduzir o total abaixo do número de exemplares emprestados.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou total menor que os exemplares emprestados")
    @ApiResponse(responseCode = "404", description = "Livro ou autor não encontrado")
    public LivroResponse atualizar(@Parameter(description = "ID do livro", example = "1") @PathVariable Long id,
                                   @Valid @RequestBody LivroRequest req) {
        return livroService.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir livro",
            description = "Não é possível excluir um livro que possui empréstimos registrados.")
    @ApiResponse(responseCode = "400", description = "O livro possui empréstimos registrados")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    public void excluir(@Parameter(description = "ID do livro", example = "1") @PathVariable Long id) {
        livroService.excluir(id);
    }
}