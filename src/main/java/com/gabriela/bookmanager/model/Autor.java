package com.gabriela.bookmanager.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "autores")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Autor cadastrado")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador do autor", example = "1")
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false)
    @Schema(description = "Nome completo do autor", example = "Machado de Assis")
    private String nome;

    @Schema(description = "Nacionalidade do autor", example = "Brasileira")
    private String nacionalidade;
}