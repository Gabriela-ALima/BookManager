package com.gabriela.bookmanager.repository;

import com.gabriela.bookmanager.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByAutorId(Long autorId);

    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    List<Livro> findByAutorNomeContainingIgnoreCase(String nome);

    List<Livro> findByGeneroContainingIgnoreCase(String genero);

    boolean existsByAutorId(Long autorId);
}