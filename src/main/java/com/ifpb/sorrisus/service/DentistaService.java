package com.ifpb.sorrisus.service;

import com.ifpb.sorrisus.exception.*;
import com.ifpb.sorrisus.model.Dentista;
import com.ifpb.sorrisus.repository.DentistaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DentistaService {

    private final DentistaRepository dentistaRepository;

    public DentistaService(DentistaRepository dentistaRepository) {
        this.dentistaRepository = dentistaRepository;
    }

    private boolean croValido(String cro) {
        return cro != null && cro.matches("\\d{4,6}-[A-Z]{2}");
    }

    public Dentista salvar(Dentista dentista) {

        if (dentista.getEmail() == null || dentista.getEmail().isBlank()) {
            throw new InvalidFieldException("email", "O email é obrigatório.");
        }
        if (dentistaRepository.existsByEmail(dentista.getEmail())) {
            throw new EmailAlreadyExistsException(dentista.getEmail());
        }

        if (dentista.getCro() == null) {
            throw new InvalidFieldException("cro", "O CRO é obrigatório.");
        }

        if (!croValido(dentista.getCro())) {
            throw new InvalidCROException("Formato inválido de CRO. Exemplo válido: 12345-PB");
        }

        if (dentistaRepository.existsByCro(dentista.getCro())) {
            throw new BusinessException("O CRO '" + dentista.getCro() + "' já está cadastrado.");
        }

        if (dentista.getEspecialidade() == null || dentista.getEspecialidade().isBlank()) {
            throw new InvalidFieldException("especialidade", "A especialidade é obrigatória.");
        }

        return dentistaRepository.save(dentista);
    }

    public Dentista buscarPorId(Long id) {
        return dentistaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Dentista com id " + id + " não encontrado.")
        );
    }

    public List<Dentista> listarTodos() {
        return dentistaRepository.findAll();
    }

    public Dentista atualizar(Long id, Dentista atualizado) {
        Dentista existente = buscarPorId(id);

        if (!existente.getEmail().equals(atualizado.getEmail())
                && dentistaRepository.existsByEmail(atualizado.getEmail())) {
            throw new EmailAlreadyExistsException(atualizado.getEmail());
        }

        if (!existente.getCro().equals(atualizado.getCro()) &&
                dentistaRepository.existsByCro(atualizado.getCro())) {
            throw new BusinessException("O CRO '" + atualizado.getCro() + "' já está cadastrado.");
        }

        if (!croValido(atualizado.getCro())) {
            throw new InvalidCROException("Formato inválido de CRO. Exemplo válido: 12345-PB");
        }

        existente.setNome(atualizado.getNome());
        existente.setEspecialidade(atualizado.getEspecialidade());
        existente.setCro(atualizado.getCro());

        return dentistaRepository.save(existente);
    }

    public void deletar(Long id) {
        Dentista dentista = buscarPorId(id);
        dentistaRepository.delete(dentista);
    }
}
