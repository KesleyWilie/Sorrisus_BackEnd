package com.ifpb.sorrisus.service;

import com.ifpb.sorrisus.model.Dentista;
import com.ifpb.sorrisus.repository.DentistaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DentistaService {

    private final DentistaRepository repository;

    public DentistaService(DentistaRepository repository) {
        this.repository = repository;
    }

    public List<Dentista> listarTodos() {
        return repository.findAll();
    }

    public Optional<Dentista> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Dentista salvar(Dentista dentista) {
        return repository.save(dentista);
    }

    public Dentista atualizar(Long id, Dentista dentistaAtualizado) {
        return repository.findById(id)
                .map(dentista -> {
                    dentista.setNome(dentistaAtualizado.getNome());
                    dentista.setEmail(dentistaAtualizado.getEmail());
                    dentista.setSenha(dentistaAtualizado.getSenha());
                    dentista.setRole(dentistaAtualizado.getRole());
                    dentista.setCro(dentistaAtualizado.getCro());
                    dentista.setEspecialidade(dentistaAtualizado.getEspecialidade());
                    return repository.save(dentista);
                })
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
