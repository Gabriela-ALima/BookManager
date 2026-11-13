package com.gabriela.bookmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para cadastrar ou atualizar um autor")
public record AutorRequest(
        @Schema(description = "Nome completo do autor", example = "Machado de Assis")
        @NotBlank(message = "O nome é obrigatório") String nome,

        @Schema(description = "Nacionalidade do autor", example = "Brasileira")
        String nacionalidade
) {}