package com.ifpb.sorrisus.service;

import com.ifpb.sorrisus.model.Paciente;
import com.ifpb.sorrisus.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    public List<Paciente> listarTodos() {
        return repository.findAll();
    }

    public Optional<Paciente> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Paciente salvar(Paciente paciente) {
        return repository.save(paciente);
    }

    public Paciente atualizar(Long id, Paciente pacienteAtualizado) {
        return repository.findById(id)
                .map(paciente -> {
                    paciente.setNome(pacienteAtualizado.getNome());
                    paciente.setEmail(pacienteAtualizado.getEmail());
                    paciente.setSenha(pacienteAtualizado.getSenha());
                    paciente.setRole(pacienteAtualizado.getRole());
                    paciente.setCpf(pacienteAtualizado.getCpf());
                    paciente.setTelefone(pacienteAtualizado.getTelefone());
                    paciente.setDataNascimento(pacienteAtualizado.getDataNascimento());
                    return repository.save(paciente);
                })
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
