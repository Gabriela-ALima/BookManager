package com.gabriela.bookmanager.controller;

import com.gabriela.bookmanager.dto.EmprestimoRequest;
import com.gabriela.bookmanager.dto.EmprestimoResponse;
import com.gabriela.bookmanager.model.StatusEmprestimo;
import com.gabriela.bookmanager.service.EmprestimoService;
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
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
@Tag(name = "Empréstimos", description = "Registro de empréstimos e devoluções")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @GetMapping
    @Operation(summary = "Listar empréstimos",
            description = "Retorna todos os empréstimos. Use o filtro status para ver só os ativos ou só os devolvidos.")
    public List<EmprestimoResponse> listar(
            @Parameter(description = "Filtrar pela situação do empréstimo", example = "ATIVO")
            @RequestParam(required = false) StatusEmprestimo status) {
        return emprestimoService.listar(status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar empréstimo",
            description = "Reduz em 1 a quantidade disponível do livro. Não é permitido emprestar quando "
                    + "a quantidade disponível é zero.")
    @ApiResponse(responseCode = "400",
            description = "Dados inválidos, data anterior a hoje ou livro sem exemplares disponíveis")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    public EmprestimoResponse registrar(@Valid @RequestBody EmprestimoRequest req) {
        return emprestimoService.registrar(req);
    }

    @PutMapping("/{id}/devolucao")
    @Operation(summary = "Registrar devolução",
            description = "Marca o empréstimo como DEVOLVIDO, grava a data de devolução e aumenta em 1 "
                    + "a quantidade disponível do livro.")
    @ApiResponse(responseCode = "400", description = "O empréstimo já foi devolvido")
    @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")
    public EmprestimoResponse devolver(
            @Parameter(description = "ID do empréstimo (não do livro)", example = "1") @PathVariable Long id) {
        return emprestimoService.devolver(id);
    }
}