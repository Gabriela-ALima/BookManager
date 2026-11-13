package com.gabriela.bookmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "Dados para registrar um empréstimo")
public record EmprestimoRequest(
        @Schema(description = "ID de um livro já cadastrado", example = "1")
        @NotNull(message = "O livro é obrigatório") Long livroId,

        @Schema(description = "Data prevista para devolução (hoje ou futura), no formato AAAA-MM-DD",
                example = "2026-11-27")
        @NotNull(message = "A data prevista de devolução é obrigatória") LocalDate dataPrevistaDevolucao
) {}