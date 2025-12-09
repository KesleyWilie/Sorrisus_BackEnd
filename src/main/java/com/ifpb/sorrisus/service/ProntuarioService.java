package com.ifpb.sorrisus.service;

import com.ifpb.sorrisus.dto.ProntuarioDTO;
import com.ifpb.sorrisus.exception.ResourceNotFoundException;
import com.ifpb.sorrisus.model.*;
import com.ifpb.sorrisus.repository.ConsultaRepository;
import com.ifpb.sorrisus.repository.ProntuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final ConsultaRepository consultaRepository;
    private final AnamneseService anamneseService; 

    public ProntuarioService(ProntuarioRepository prontuarioRepository,
                             ConsultaRepository consultaRepository,
                             AnamneseService anamneseService) {
        this.prontuarioRepository = prontuarioRepository;
        this.consultaRepository = consultaRepository;
        this.anamneseService = anamneseService;
    }

    @Transactional
    public ProntuarioDTO salvarFichaClinica(ProntuarioDTO dto, Long consultaId) {
        if (consultaId == null) {
            throw new IllegalArgumentException("O ID da consulta é obrigatório para registrar o prontuário.");
        }
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada."));

        Paciente paciente = consulta.getPaciente();

        Anamnese anamneseSalva = anamneseService.salvarOuAtualizar(paciente, dto);

        Prontuario prontuario = consulta.getProntuario();
        if (prontuario == null) {
            prontuario = new Prontuario();
            prontuario.setConsulta(consulta);
        }

        prontuario.setObservacoes(dto.getObservacoes());
        prontuario.setPlanoTratamento(dto.getPlanoTratamento());
        prontuario.setOdontogramaJson(dto.getOdontogramaJson());

        Prontuario prontuarioSalvo = prontuarioRepository.save(prontuario);
        
        if (consulta.getProntuario() == null) {
            consulta.setProntuario(prontuarioSalvo);
            consultaRepository.save(consulta);
        }

        return montarDTO(anamneseSalva, prontuarioSalvo);
    }

    public ProntuarioDTO buscarPorConsultaId(Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada."));

        Prontuario prontuario = consulta.getProntuario();
        
        if (prontuario == null) {
             throw new ResourceNotFoundException("Nenhum prontuário registrado para esta consulta.");
        }

        Anamnese anamnese = anamneseService.buscarPorPaciente(consulta.getPaciente());

        return montarDTO(anamnese, prontuario);
    }
    
    @Transactional
    public void deletar(Long id) {
        Prontuario p = prontuarioRepository.findById(id)
             .orElseThrow(() -> new ResourceNotFoundException("Prontuário não encontrado"));
        
        if(p.getConsulta() != null) {
            p.getConsulta().setProntuario(null);
            consultaRepository.save(p.getConsulta());
        }
        prontuarioRepository.delete(p);
    }

    private ProntuarioDTO montarDTO(Anamnese a, Prontuario p) {
        ProntuarioDTO dto = new ProntuarioDTO();
        
        dto.setId(p.getId());
        dto.setObservacoes(p.getObservacoes());
        dto.setPlanoTratamento(p.getPlanoTratamento());
        dto.setOdontogramaJson(p.getOdontogramaJson());

        if (a != null) {
            dto.setAlergiaResposta(a.getAlergiaResposta());
            dto.setAlergiaNotas(a.getAlergiaNotas());
            dto.setAntibioticoResposta(a.getAntibioticoResposta());
            dto.setAntibioticoNotas(a.getAntibioticoNotas());
            dto.setAnestesicoResposta(a.getAnestesicoResposta());
            dto.setAnestesicoNotas(a.getAnestesicoNotas());
            dto.setSensibilidadeResposta(a.getSensibilidadeResposta());
            dto.setSensibilidadeNotas(a.getSensibilidadeNotas());
            dto.setPressaoResposta(a.getPressaoResposta());
            dto.setPressaoNotas(a.getPressaoNotas());
            dto.setMedicamentoResposta(a.getMedicamentoResposta());
            dto.setMedicamentoNotas(a.getMedicamentoNotas());
            dto.setProblemaSaudeResposta(a.getProblemaSaudeResposta());
            dto.setProblemaSaudeNotas(a.getProblemaSaudeNotas());
        }
        return dto;
    }
}