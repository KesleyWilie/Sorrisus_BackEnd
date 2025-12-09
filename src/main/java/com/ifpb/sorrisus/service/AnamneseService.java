package com.ifpb.sorrisus.service;

import com.ifpb.sorrisus.dto.ProntuarioDTO;
import com.ifpb.sorrisus.model.Anamnese;
import com.ifpb.sorrisus.model.Paciente;
import com.ifpb.sorrisus.repository.AnamneseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnamneseService {

    private final AnamneseRepository anamneseRepository;

    public AnamneseService(AnamneseRepository anamneseRepository) {
        this.anamneseRepository = anamneseRepository;
    }

    public Anamnese buscarPorPaciente(Paciente paciente) {
        return anamneseRepository.findByPaciente(paciente)
                .orElse(new Anamnese());
    }

    @Transactional
    public Anamnese salvarOuAtualizar(Paciente paciente, ProntuarioDTO dto) {
        Anamnese anamnese = anamneseRepository.findByPaciente(paciente)
                .orElse(new Anamnese());

        anamnese.setPaciente(paciente);

        anamnese.setAlergiaResposta(dto.getAlergiaResposta());
        anamnese.setAlergiaNotas(dto.getAlergiaNotas());

        anamnese.setAntibioticoResposta(dto.getAntibioticoResposta());
        anamnese.setAntibioticoNotas(dto.getAntibioticoNotas());

        anamnese.setAnestesicoResposta(dto.getAnestesicoResposta());
        anamnese.setAnestesicoNotas(dto.getAnestesicoNotas());

        anamnese.setSensibilidadeResposta(dto.getSensibilidadeResposta());
        anamnese.setSensibilidadeNotas(dto.getSensibilidadeNotas());

        anamnese.setPressaoResposta(dto.getPressaoResposta());
        anamnese.setPressaoNotas(dto.getPressaoNotas());

        anamnese.setMedicamentoResposta(dto.getMedicamentoResposta());
        anamnese.setMedicamentoNotas(dto.getMedicamentoNotas());

        anamnese.setProblemaSaudeResposta(dto.getProblemaSaudeResposta());
        anamnese.setProblemaSaudeNotas(dto.getProblemaSaudeNotas());

        return anamneseRepository.save(anamnese);
    }
}