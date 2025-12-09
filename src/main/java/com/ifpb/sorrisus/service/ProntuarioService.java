package com.ifpb.sorrisus.service;

import com.ifpb.sorrisus.exception.ResourceNotFoundException;
import com.ifpb.sorrisus.model.Consulta;
import com.ifpb.sorrisus.model.Prontuario;
import com.ifpb.sorrisus.repository.ConsultaRepository;
import com.ifpb.sorrisus.repository.ProntuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final ConsultaRepository consultaRepository;

    public ProntuarioService(ProntuarioRepository prontuarioRepository,
                             ConsultaRepository consultaRepository) {
        this.prontuarioRepository = prontuarioRepository;
        this.consultaRepository = consultaRepository;
    }

    @Transactional
    public Prontuario criar(Prontuario prontuario, Long consultaId) {
        Prontuario salvo = prontuarioRepository.save(prontuario);

        if (consultaId != null) {
            Consulta consulta = consultaRepository.findById(consultaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Consulta com id " + consultaId + " não encontrada."));
            consulta.setProntuario(salvo);
            consultaRepository.save(consulta);
        }

        return salvo;
    }

    public Prontuario buscarPorId(Long id) {
        return prontuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prontuário com id " + id + " não encontrado."));
    }

    @Transactional
    public Prontuario atualizar(Long id, Prontuario atualizado) {
        Prontuario existente = buscarPorId(id);

        existente.setAlergiaResposta(atualizado.getAlergiaResposta());
        existente.setAlergiaNotas(atualizado.getAlergiaNotas());

        existente.setAntibioticoResposta(atualizado.getAntibioticoResposta());
        existente.setAntibioticoNotas(atualizado.getAntibioticoNotas());

        existente.setAnestesicoResposta(atualizado.getAnestesicoResposta());
        existente.setAnestesicoNotas(atualizado.getAnestesicoNotas());

        existente.setSensibilidadeResposta(atualizado.getSensibilidadeResposta());
        existente.setSensibilidadeNotas(atualizado.getSensibilidadeNotas());

        existente.setPressaoResposta(atualizado.getPressaoResposta());
        existente.setPressaoNotas(atualizado.getPressaoNotas());

        existente.setMedicamentoResposta(atualizado.getMedicamentoResposta());
        existente.setMedicamentoNotas(atualizado.getMedicamentoNotas());

        existente.setProblemaSaudeResposta(atualizado.getProblemaSaudeResposta());
        existente.setProblemaSaudeNotas(atualizado.getProblemaSaudeNotas());

        existente.setObservacoes(atualizado.getObservacoes());
        existente.setPlanoTratamento(atualizado.getPlanoTratamento());

        existente.setOdontogramaJson(atualizado.getOdontogramaJson());

        return prontuarioRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Prontuario existente = buscarPorId(id);
        Consulta consulta = consultaRepository.findAll().stream()
                .filter(c -> c.getProntuario() != null && c.getProntuario().getId().equals(existente.getId()))
                .findFirst().orElse(null);
        if (consulta != null) {
            consulta.setProntuario(null);
            consultaRepository.save(consulta);
        }
        prontuarioRepository.delete(existente);
    }
}
