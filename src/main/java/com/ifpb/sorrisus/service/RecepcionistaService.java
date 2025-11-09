package com.ifpb.sorrisus.service;

import com.ifpb.sorrisus.model.Recepcionista;
import com.ifpb.sorrisus.repository.RecepcionistaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecepcionistaService {

    private final RecepcionistaRepository repository;

    public RecepcionistaService(RecepcionistaRepository repository) {
        this.repository = repository;
    }

    public List<Recepcionista> listarTodos() {
        return repository.findAll();
    }

    public Optional<Recepcionista> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Recepcionista salvar(Recepcionista recepcionista) {
        return repository.save(recepcionista);
    }

    public Recepcionista atualizar(Long id, Recepcionista recepcionistaAtualizado) {
        return repository.findById(id)
                .map(recepcionista -> {
                    recepcionista.setNome(recepcionistaAtualizado.getNome());
                    recepcionista.setEmail(recepcionistaAtualizado.getEmail());
                    recepcionista.setSenha(recepcionistaAtualizado.getSenha());
                    recepcionista.setRole(recepcionistaAtualizado.getRole());
                    return repository.save(recepcionista);
                })
                .orElseThrow(() -> new RuntimeException("Recepcionista não encontrado"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
