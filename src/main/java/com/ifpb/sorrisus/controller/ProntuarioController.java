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
        Prontuario p = new Prontuario();
        p.setObservacoes(dto.getObservacoes());

        Prontuario salvo = prontuarioService.criar(p, consultaId);

        ProntuarioDTO resp = toDTO(salvo);
        return ResponseEntity.created(URI.create("/api/prontuarios/" + salvo.getId())).body(resp);
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
        dto.setObservacoes(p.getObservacoes());
        return dto;
    }
}
