package com.gabriela.bookmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "livros")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Column(nullable = false)
    private String titulo;

    private String isbn;

    private Integer anoPublicacao;

    private String genero;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer quantidadeTotal;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer quantidadeDisponivel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "autor_id")
    private Autor autor;
}