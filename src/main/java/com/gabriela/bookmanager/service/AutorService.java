package com.gabriela.bookmanager.service;

import com.gabriela.bookmanager.dto.AutorRequest;
import com.gabriela.bookmanager.dto.LivroResponse;
import com.gabriela.bookmanager.exception.RecursoNaoEncontradoException;
import com.gabriela.bookmanager.exception.RegraNegocioException;
import com.gabriela.bookmanager.model.Autor;
import com.gabriela.bookmanager.repository.AutorRepository;
import com.gabriela.bookmanager.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;

    public List<Autor> listar() {
        return autorRepository.findAll();
    }

    public Autor buscarPorId(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado: " + id));
    }

    @Transactional
    public Autor criar(AutorRequest req) {
        Autor autor = new Autor();
        autor.setNome(req.nome());
        autor.setNacionalidade(req.nacionalidade());
        return autorRepository.save(autor);
    }

    @Transactional
    public Autor atualizar(Long id, AutorRequest req) {
        Autor autor = buscarPorId(id);
        autor.setNome(req.nome());
        autor.setNacionalidade(req.nacionalidade());
        return autorRepository.save(autor);
    }

    @Transactional
    public void excluir(Long id) {
        Autor autor = buscarPorId(id);
        if (livroRepository.existsByAutorId(id)) {
            throw new RegraNegocioException("Não é possível excluir um autor que possui livros cadastrados");
        }
        autorRepository.delete(autor);
    }

    public List<LivroResponse> listarLivros(Long autorId) {
        buscarPorId(autorId);
        return livroRepository.findByAutorId(autorId).stream()
                .map(LivroResponse::from)
                .toList();
    }
}