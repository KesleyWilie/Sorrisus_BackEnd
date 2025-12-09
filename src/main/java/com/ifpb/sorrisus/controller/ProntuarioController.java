package com.ifpb.sorrisus.controller;

import com.ifpb.sorrisus.dto.ProntuarioDTO;
import com.ifpb.sorrisus.model.Prontuario;
import com.ifpb.sorrisus.service.ProntuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.net.URI;

@RestController
@RequestMapping("/api/prontuarios")
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    public ProntuarioController(ProntuarioService prontuarioService) {
        this.prontuarioService = prontuarioService;
    }

    @PostMapping
    @PreAuthorize("hasRole('DENTISTA')")
    public ResponseEntity<ProntuarioDTO> criar(@Valid @RequestBody ProntuarioDTO dto,
                                               @RequestParam(required = false) Long consultaId) {
        Prontuario p = fromDTO(dto);

        Prontuario salvo = prontuarioService.criar(p, consultaId);

        return ResponseEntity.created(URI.create("/api/prontuarios/" + salvo.getId()))
                .body(toDTO(salvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProntuarioDTO> buscarPorId(@PathVariable Long id) {
        Prontuario p = prontuarioService.buscarPorId(id);
        return ResponseEntity.ok(toDTO(p));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DENTISTA')")
    public ResponseEntity<ProntuarioDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProntuarioDTO dto) {
        Prontuario atualizado = new Prontuario();
        atualizado.setObservacoes(dto.getObservacoes());

        Prontuario salvo = prontuarioService.atualizar(id, atualizado);
        return ResponseEntity.ok(toDTO(salvo));
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DENTISTA')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        prontuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private ProntuarioDTO toDTO(Prontuario p) {
        ProntuarioDTO dto = new ProntuarioDTO();
        
        dto.setId(p.getId());

        dto.setAlergiaResposta(p.getAlergiaResposta());
        dto.setAlergiaNotas(p.getAlergiaNotas());

        dto.setAntibioticoResposta(p.getAntibioticoResposta());
        dto.setAntibioticoNotas(p.getAntibioticoNotas());

        dto.setAnestesicoResposta(p.getAnestesicoResposta());
        dto.setAnestesicoNotas(p.getAnestesicoNotas());

        dto.setSensibilidadeResposta(p.getSensibilidadeResposta());
        dto.setSensibilidadeNotas(p.getSensibilidadeNotas());

        dto.setPressaoResposta(p.getPressaoResposta());
        dto.setPressaoNotas(p.getPressaoNotas());

        dto.setMedicamentoResposta(p.getMedicamentoResposta());
        dto.setMedicamentoNotas(p.getMedicamentoNotas());

        dto.setProblemaSaudeResposta(p.getProblemaSaudeResposta());
        dto.setProblemaSaudeNotas(p.getProblemaSaudeNotas());

        dto.setObservacoes(p.getObservacoes());
        dto.setPlanoTratamento(p.getPlanoTratamento());

        dto.setOdontogramaJson(p.getOdontogramaJson());

        return dto;
    }

    private Prontuario fromDTO(ProntuarioDTO dto) {
        Prontuario p = new Prontuario();

        p.setAlergiaResposta(dto.getAlergiaResposta());
        p.setAlergiaNotas(dto.getAlergiaNotas());

        p.setAntibioticoResposta(dto.getAntibioticoResposta());
        p.setAntibioticoNotas(dto.getAntibioticoNotas());

        p.setAnestesicoResposta(dto.getAnestesicoResposta());
        p.setAnestesicoNotas(dto.getAnestesicoNotas());

        p.setSensibilidadeResposta(dto.getSensibilidadeResposta());
        p.setSensibilidadeNotas(dto.getSensibilidadeNotas());

        p.setPressaoResposta(dto.getPressaoResposta());
        p.setPressaoNotas(dto.getPressaoNotas());

        p.setMedicamentoResposta(dto.getMedicamentoResposta());
        p.setMedicamentoNotas(dto.getMedicamentoNotas());

        p.setProblemaSaudeResposta(dto.getProblemaSaudeResposta());
        p.setProblemaSaudeNotas(dto.getProblemaSaudeNotas());

        p.setObservacoes(dto.getObservacoes());
        p.setPlanoTratamento(dto.getPlanoTratamento());

        p.setOdontogramaJson(dto.getOdontogramaJson());

        return p;
    }

}
