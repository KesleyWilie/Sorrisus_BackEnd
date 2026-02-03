package com.ifpb.sorrisus.controller;

import com.ifpb.sorrisus.dto.ProntuarioDTO;
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
    public ResponseEntity<ProntuarioDTO> salvar(@Valid @RequestBody ProntuarioDTO dto,
                                                @RequestParam Long consultaId) {
        
        ProntuarioDTO salvo = prontuarioService.salvarFichaClinica(dto, consultaId);

        return ResponseEntity.created(URI.create("/api/prontuarios/" + salvo.getId()))
                .body(salvo);
    }

    @GetMapping("/consulta/{consultaId}")
    public ResponseEntity<ProntuarioDTO> buscarPorConsulta(@PathVariable Long consultaId) {
        ProntuarioDTO dto = prontuarioService.buscarPorConsultaId(consultaId);
        return ResponseEntity.ok(dto);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DENTISTA')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        prontuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}