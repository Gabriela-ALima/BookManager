package com.gabriela.bookmanager.repository;

import com.gabriela.bookmanager.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
}