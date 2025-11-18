package com.ifpb.sorrisus.service;

import com.ifpb.sorrisus.exception.*;
import com.ifpb.sorrisus.model.Recepcionista;
import com.ifpb.sorrisus.repository.RecepcionistaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecepcionistaService {

    private final RecepcionistaRepository recepcionistaRepository;

    public RecepcionistaService(RecepcionistaRepository recepcionistaRepository) {
        this.recepcionistaRepository = recepcionistaRepository;
    }

    private boolean turnoValido(String turno) {
        return turno != null &&
                (turno.equalsIgnoreCase("MANHA") ||
                 turno.equalsIgnoreCase("TARDE") ||
                 turno.equalsIgnoreCase("NOITE"));
    }

    public Recepcionista salvar(Recepcionista recepcionista) {

        if (recepcionista.getTurno() == null) {
            throw new InvalidFieldException("turno", "O turno é obrigatório.");
        }

        if (!turnoValido(recepcionista.getTurno())) {
            throw new InvalidFieldException("turno",
                    "Turno inválido. Valores aceitos: MANHA, TARDE, NOITE.");
        }

        return recepcionistaRepository.save(recepcionista);
    }

    public Recepcionista buscarPorId(Long id) {
        return recepcionistaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recepcionista com id " + id + " não encontrado.")
        );
    }

    public List<Recepcionista> listarTodos() {
        return recepcionistaRepository.findAll();
    }

    public Recepcionista atualizar(Long id, Recepcionista atualizado) {
        Recepcionista existente = buscarPorId(id);

        if (!turnoValido(atualizado.getTurno())) {
            throw new InvalidFieldException("turno",
                    "Turno inválido. Valores aceitos: MANHA, TARDE, NOITE.");
        }

        existente.setNome(atualizado.getNome());
        existente.setTurno(atualizado.getTurno());

        return recepcionistaRepository.save(existente);
    }

    public void deletar(Long id) {
        Recepcionista recepcionista = buscarPorId(id);
        recepcionistaRepository.delete(recepcionista);
    }
}
